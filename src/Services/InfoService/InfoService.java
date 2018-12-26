package Services.InfoService;

import DB.DBHandler;
import DB.Job;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@WebService(serviceName = "InfoService")
public class InfoService {

    @WebMethod
    public Job getjob(@XmlElement(required = true, nillable = false)  Integer jobId)throws Exception {
        DBHandler handler =new DBHandler();
        Job job =handler.getJob(jobId.intValue());

        return job;
    }

}
