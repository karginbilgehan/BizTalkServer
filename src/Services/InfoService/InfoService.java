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
    @XmlElement(name ="Job")
    public ArrayList<JobResponse> getJob(Integer jobId) throws Exception{

        DBHandler handler =new DBHandler();
        Job job =handler.getJob(jobId.intValue());
        JobResponse info =new JobResponse();
        info.setJobId(jobId);
        info.setJobDescription(job.getDescription());
        info.setJobOwner(job.getOwner());
        info.setRelatives(job.getRelatives());
        info.setStatus(job.getStatus());

        ArrayList<JobResponse> list = new ArrayList();
        list.add(info);
        list.add(info);
        return list;
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
