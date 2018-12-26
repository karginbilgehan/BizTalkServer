package Services.InfoService;

import DB.DBHandler;
import DB.Job;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@WebService(serviceName = "InfoService")
public class InfoService {

   @WebMethod
    @XmlElement(name ="getJob")
    public Job getJob(Integer jobId) throws Exception{

        DBHandler handler =new DBHandler();
        Job job =handler.getJob(jobId.intValue());
        
        return job;
    }
    
    @WebMethod
    @XmlElement(name ="getOrchestration")
    public Set<Orchestration> getOrchestration(Integer ownerId) throws Exception{
    
        DBHandler handler =new DBHandler();
        Set<Orchestration> orc =handler.getOrchestration(ownerId.intValue());

        return orc;
    }
    
    @WebMethod
    @XmlElement(name ="getRule")
    public Rule getRule(Integer ruleId) throws Exception{
    
        DBHandler handler =new DBHandler();
        Rule rule =handler.getRule(ruleId.intValue());
        
        return rule;
    }



}
