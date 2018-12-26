package Services.InfoService;

import DB.DBHandler;
import DB.Job;
import DB.Orchestration;
import DB.Rule;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Set;

@WebService(serviceName = "InfoServicex")
public class InfoService {

      @WebMethod
    @XmlElement(name ="getJob")
    public JobResponse getJob(Integer jobId) throws Exception{

        DBHandler handler =new DBHandler();
        Job job =handler.getJob(jobId.intValue());
        JobResponse info =new JobResponse();
        info.setDescription(job.getDescription());
        info.setDestination(job.getDestination());
        info.setFileUrl(job.getFileUrl());
        info.setId(job.getId());
        info.setInsertDateTime(job.getInsertDateTime());
        info.setOwner(job.getOwner());
        info.setRelatives(job.getRelatives());
        info.setRuleId(job.getRuleId());
        info.setStatus(job.getStatus());
     //   info.setUpdateDateTime(job.getUpdateDateTime());
        
        return info;
    }
    
    @WebMethod
    @XmlElement(name ="getOrchestration")
    public ArrayList<Orchestration> getOrchestration(Integer ownerId) throws Exception{
    
        DBHandler handler =new DBHandler();
        ArrayList<Orchestration> orc =handler.getOrchestration(ownerId.intValue());

        return orc;
    }
    
    @WebMethod
    @XmlElement(name ="getRule")
    public RuleResponse getRule(Integer ruleId) throws Exception{
    
        DBHandler handler =new DBHandler();
        Rule rule =handler.getRule(ruleId.intValue());
        RuleResponse info =new RuleResponse();
        
        info.setId(rule.getId());
        info.setNoEdge(rule.getNoEdge());
        info.setOwnerID(rule.getOwnerID());
        rule.setQuery(rule.getQuery());
        rule.setRelativeResults(rule.getRelativeResults());
        rule.setYesEdge(rule.getYesEdge());
        
        return info;
    }




}
