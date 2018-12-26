package Services.Orchestration;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

import DB.*;
import Services.Orchestration.Requests.*;

@WebService(endpointInterface = "Services.Orchestration.IOrchestrationService",
        serviceName = "OrchestrationService")
public class OrchestrationService implements IOrchestrationService {


    private DBHandler dbHandler = new DBHandler();

    /**
     * Introduce an orchestration.
     *
     * @param value Object that contains orchestration information.
     * @param jobRequests  List that contains jobRequests of orchestration.
     * @param ruleRequests List that contains ruleRequests of orchestration.
     * @return Message.
     */
    @Override
    public String addOrchestration(OrchestrationRequest value, List<JobRequest> jobRequests, List<RuleRequest> ruleRequests) {
        List<Integer> JobIdList = new ArrayList<>();
        List<Integer> RuleIdList = new ArrayList<>();

        //End nodes
        JobIdList.add(0);
        RuleIdList.add(0);

        //Saving jobRequests to the database
        // and adding their generated id's from db to JobIdList.
        for (JobRequest temp : jobRequests)
            JobIdList.add(addJob(temp));

        // Saving ruleRequests to db.
        // creating edges between ruleRequests declared from gui.
        // adding DB.RuleRequest id's to the RuleIdList.
        for (RuleRequest temp : ruleRequests) {
            temp.yesEdge = JobIdList.get(temp.yesEdge);
            temp.noEdge = JobIdList.get(temp.noEdge);

            RuleIdList.add(addRule(temp));
        }

        //Updating all jobRequests with their associated ruleRequests with db ids.
        for(int jobId : JobIdList){
            if (jobId == 0) continue;

            Job workOn;
            try {
                workOn = dbHandler.getJob(jobId);
            } catch (Exception e){
                System.err.println("Error to access given id: " + jobId);
                System.err.println(e);
                return String.format("*** Error to access given id: %d***", jobId);
            }

            int ruleId = RuleIdList.get(workOn.getRuleId() - 1);
            try {
                dbHandler.updateJob(jobId, "RuleId", ruleId);
            } catch (Exception e) {
                System.err.println("Error to access given id: " + ruleId);
                System.err.println(e);
                return String.format("*** Error to access given id: %d***", ruleId);
            }
        }

        // Set the start job's id.
        Orchestration actualOrch = new Orchestration(value.ownerID, 0, JobIdList.get(0));

        // Adding orchestration value to db.
        try {
            dbHandler.insertOrchestration(actualOrch);
        } catch (Exception e) {
            System.err.println("DB.OrchestrationRequest could not be introduced: " + e);
            return "*** DB.OrchestrationRequest could not be introduced. ***";
        }
        return "DB.OrchestrationRequest has been introduced successfully!";
    }

    /**
     * Add given job to database.
     *
     * @param value DB.JobRequest to be added to database.
     * @return If added, returns job id which is got from db, otherwise -1 to indicate an error.
     */
    // Bu method eklenen job ın id sini döndürmeli ancak dbden öyle bi bilgi alamıyorum.
    private int addJob(JobRequest value) {
        int dbJobId;
        Job actualJob = new Job(value.owner, value.description, value.destination, value.fileUrl, value.relatives, 0, 0);
        try {
            dbJobId = dbHandler.insertJob(actualJob);
        } catch (Exception e) {
            System.err.println("DB.JobRequest Db insert error: " + e);
            return -1;
        }
        actualJob.setId(dbJobId);
        return dbJobId;
    }

    /**
     * Add given rule to database.
     *
     * @param value DB.RuleRequest to be added to database.
     * @return If added, returns rule id which is got from db, otherwise -1 to indicate an error.
     */
    private int addRule(RuleRequest value) {
        int dbRuleId;
        Rule actualRule = new Rule(value.ownerID, value.query, value.yesEdge, value.noEdge, value.relativeResults);
        try {
            dbRuleId = dbHandler.insertRule(actualRule);
        } catch (Exception e) {
            System.out.println("DB.RuleRequest Db insert error: " + e);
            return -1;
        }
        actualRule.setId(dbRuleId);
        return dbRuleId;
    }

}
