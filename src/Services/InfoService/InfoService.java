package Services.InfoService;

import DB.DBHandler;
import DB.Job;
import DB.Orchestration;
import DB.Rule;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.Set;

@WebService(serviceName = "InfoServicex")
public class InfoService {

 @WebMethod
    public Job getJob(Integer jobId) throws Exception{

        DBHandler handler =new DBHandler();
        Job job =handler.getJob(jobId.intValue());
        
        return job;
    }
    
    @WebMethod
    public Set<Orchestration> getOrchestration(Integer ownerId) throws Exception{
    
        DBHandler handler =new DBHandler();
        Set<Orchestration> orc =handler.getOrchestration(ownerId.intValue());

        return orc;
    }
    
    @WebMethod
    public Rule getRule(Integer ruleId) throws Exception{
    
        DBHandler handler =new DBHandler();
        Rule rule =handler.getRule(ruleId.intValue());
        
        return rule;
    }


}
