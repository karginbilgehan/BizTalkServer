package MainProcess;

import DB.*;
import Services.StatusCodes;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.time.StopWatch;

public class MainProcess {
    private static DBHandler dbHandler = new DBHandler();

    private static final int _5min = 300000;

    private static void work(Job job) {
        System.out.println(String.format("Job: %d islendi", job.getId())); // job islendi.
    }

    private static char callBRE(Rule rule) {
      /*  String relativeResults = rule.getRelativeResults();
        List<String> resList = Arrays.asList(relativeResults.split("\\s*,\\s*"));
        if (resList.contains("X"))
            return 'X';
        if (resList.contains("F"))
            return 'F';
            */
        return 'T';
    }

    private static void orchestrationRun(Orchestration orchestration) {
        try {
            // StartJobID orchestration objesinden alinir.
            int currentJobID = orchestration.getStartJobID();

            // Baslangic jobu Db'den cekilir.
            Job currentJob = dbHandler.getJob(currentJobID);
            boolean abnormalState = false;

            while (currentJob.getRuleId() != 0) {

                // Jobun ruleu alinir.
                Rule ruleOfCurrentJob = dbHandler.getRule(currentJob.getRuleId());
                char responseOfBRE;

                StopWatch sw = StopWatch.createStarted();
                while ((responseOfBRE = callBRE(ruleOfCurrentJob)) == 'X' && sw.getTime() < _5min) {
                    ruleOfCurrentJob = dbHandler.getRule(currentJob.getRuleId());
                }
                sw.stop();
                if (responseOfBRE == 'T') {
                    work(currentJob);
                    dbHandler.updateJob(currentJobID, "Status", StatusCodes.SUCCESS);
                    currentJobID = ruleOfCurrentJob.getYesEdge();
                    if (currentJobID == 0) {
                        abnormalState = true;
                        break;
                    }
                    currentJob = dbHandler.getJob(currentJobID);
                } else {
                    dbHandler.updateJob(currentJobID, "Status", StatusCodes.ERROR);
                    currentJobID = ruleOfCurrentJob.getNoEdge();
                    if (currentJobID == 0) {
                        abnormalState = true;
                        break;
                    }
                    currentJob = dbHandler.getJob(currentJobID);
                }
            }
            // Eger en son joba kadar varilirsa, o job da islenir.
            if (!abnormalState) {
                work(currentJob);
                dbHandler.updateOrchestration(orchestration.getId(), "Status", StatusCodes.SUCCESS);  //TODO ?
                dbHandler.updateJob(currentJobID, "Status", 100);                     //TODO ?
            } else {
                dbHandler.updateOrchestration(orchestration.getId(), "Status", StatusCodes.ERROR);  //TODO ?
            }
        } catch (Exception e) {
            // TODO: log basacak.
            System.out.println(String.format("*** An error occured while getting orchestration from DB: %s ***", e));
        }
    }

    public static void main(String[] args) throws Exception {
        Publish.main(null);

       /* while (true) {
            Orchestration orchestration = dbHandler.getOrchestration();
            if (orchestration.getId() != 0) {
                // new thread
                orchestrationRun(orchestration);
            } else {
                System.out.println("No orchestration waiting!");
            }
            Thread.sleep(50);
        }*/
    }
}
