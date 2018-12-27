package MainProcess;

import DB.*;

import java.util.Arrays;
import java.util.List;

public class MainProcess {
    private static DBHandler dbHandler = new DBHandler();

    private static void work(Job job) {
        System.out.println(String.format("Job: %d islendi", job.getId())); // job islendi.
    }

    private static char callBRE(Rule rule) {
      /*  String relativeResults = rule.getRelativeResults();
        List<String> resList = Arrays.asList(relativeResults.split("\\s*,\\s*"));
        if (resList.contains("X"))
            return 'X';
        if (resList.contains("F"))
            return 'F';*/
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

                while ((responseOfBRE = callBRE(ruleOfCurrentJob)) == 'X') {
                    ruleOfCurrentJob = dbHandler.getRule(currentJob.getRuleId());
                    Thread.sleep(100);
                }

                if (responseOfBRE == 'T') {
                    work(currentJob);
                    dbHandler.updateJob(currentJobID, "Status", 100);
                    currentJobID = ruleOfCurrentJob.getYesEdge();
                    if (currentJobID == 0) {
                        abnormalState = true;
                        break;
                    }
                    currentJob = dbHandler.getJob(currentJobID);
                } else if (responseOfBRE == 'F') {
                    dbHandler.updateJob(currentJobID, "Status", 404);
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
                dbHandler.updateOrchestration(orchestration.getId(), "Status", 100);  //TODO ?
                dbHandler.updateJob(currentJobID, "Status", 100);                     //TODO ?
            } else {
                dbHandler.updateOrchestration(orchestration.getId(), "Status", 404);  //TODO ?
            }
        } catch (Exception e) {
            // TODO: log basacak.
            System.out.println(String.format("*** An error occured while getting orchestration from DB: %s ***", e));
        }
    }

    public static void main(String[] args) throws Exception {
        Publish.main(null);

        while (true) {
            Orchestration orchestration = dbHandler.getOrchestration();
            if (orchestration.getId() != 0) {
                orchestrationRun(orchestration);
            } else {
                System.out.println("No orchestration waiting!");
            }
            Thread.sleep(50);
        }
    }
}

     /*

    public static void finalUpdate(Orchestration curr) throws Exception {
        DBHandler tmp = new DBHandler();
        tmp.updateOrchestration(curr.getId(), "Status", curr.getStatus());
        tmp.updateOrchestration(curr.getId(), "StartingJobId", curr.getStartJobID());//Orchestration i kuculterek gidiyoruz.
    }

    public static void statusUpdate(Job curr) throws Exception {
        DBHandler tmp = new DBHandler();
        tmp.updateJob(curr.getId(), "Status" , curr.getStatus());
    }

    public static char toBRE (int ruleID, String ruleQuery, String currRelatives) throws Exception{
        //parametreler BRE ye yollanilcak ?
        return 'X';
    }

    public static void main(String[] args) throws Exception{
        mainProcess();
    }

    public static void sendFile(Job sent) {
        //1. demodaki yollama?
    }

    private static void mainProcess() throws Exception{

        int check = 0;


        DBHandler tmp = new DBHandler();
        Orchestration newOrch = tmp.getOrchestration();
        // while(newOrch == null) {

        //}

        Job currJob = tmp.getJob(newOrch.getStartJobID());
        Rule currRule = tmp.getRule(currJob.getRuleId());

        while(check == 0) {
            char result = ' ';
            result = toBRE( currRule.getId()  , currRule.getQuery() , currRule.getRelativeResults() );

            if(result == 'X') {
                newOrch.setStatus(100);
                finalUpdate(newOrch);//Beklemeyi db ye isledik.
                Runner thread1= new Runner(newOrch);
                thread1.start();
                while(newOrch == null) {
                    newOrch = tmp.getOrchestration();
                }
                currJob = tmp.getJob(newOrch.getStartJobID());
                currRule = tmp.getRule(currJob.getRuleId());
            } else if(result == 'T') {
                sendFile(currJob);
                currJob.setStatus(-100);//Job bitti.
                statusUpdate(currJob);
                //GUIye ekranlari kapattir.

                if(currRule.getYesEdge() != 0) {//0 End node icin.
                    currJob = tmp.getJob(currRule.getYesEdge());
                    newOrch.setStartJobID(currJob.getId());
                    newOrch.setStatus(0);
                    finalUpdate(newOrch);//Orchestrationi daralttik.
                    statusUpdate(currJob);//True edgedeki jobu hazirladik.
                } else {
                    newOrch.setStatus(-100);//Bittigini db ye yazdik.
                    finalUpdate(newOrch);

                    newOrch=null;
                    //yeni is alma
                    while(newOrch == null) {
                        newOrch = tmp.getOrchestration();
                    }

                    currJob = tmp.getJob(newOrch.getStartJobID());
                    currRule = tmp.getRule(currJob.getRuleId());
                }
            } else if(result == 'F') {
                //GUIye ekranlari kapattir
                if(currRule.getNoEdge() != 0) {//0 = END EDGE
                    currJob = tmp.getJob(currRule.getNoEdge());
                    newOrch.setStartJobID(currJob.getId());
                    newOrch.setStatus(0);
                    finalUpdate(newOrch);//Orchestrationi daralttik.
                    statusUpdate(currJob);//True edgedeki jobu hazirladik.

                }
                else {//job bittiyse statusu -100 e cektik.
                    newOrch.setStatus(-100);
                    finalUpdate(newOrch);

                    newOrch=null;
                    //yeni is alcaz
                    while(newOrch == null) {
                        newOrch = tmp.getOrchestration();
                    }

                    currJob = tmp.getJob(newOrch.getStartJobID());
                    currRule = tmp.getRule(currJob.getRuleId());
                }
            }
        }
    }

    */
