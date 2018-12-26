package Services.InfoService;

import DB.Job;
import DB.Rule;

import java.util.ArrayList;

public class RulesAndJobs extends OrchestrationResponse {

    private ArrayList<Rule> rules;
    private ArrayList<Job> jobs;

    public void addJob(Job job){
        jobs.add(job);
    }

    public void addRule(Rule rule){
        rules.add(rule);
    }

}
