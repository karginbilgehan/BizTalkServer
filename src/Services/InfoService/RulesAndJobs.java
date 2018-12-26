package Services.InfoService;

import DB.Job;
import DB.Rule;

import java.util.ArrayList;

public class RulesAndJobs extends OrchestrationResponse {

    private ArrayList<Rule> rules = new ArrayList<Rule>();
    private ArrayList<Job> jobs = new ArrayList<Job>();

    public void addJob(Job job){
        jobs.add(job);
    }

    public void addRule(Rule rule){
        rules.add(rule);
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }
}
