import DB.DBHandler;
import DB.Job;
import Services.Orchestration.OrchestrationService;

import javax.xml.ws.Endpoint;

public class Publish {

    public static void main(String[] argv) throws Exception {
        Object implementor = new OrchestrationService();
        String address = "http://localhost:9001/OrchestrationService";
        Endpoint.publish(address, implementor);

        DBHandler dbHandler = new DBHandler();
        Job job = new Job(151044083,"Açıklama yazısıdır1.","10.0.0.1,10.0.0.2,10.0.0.3","http://www.cevkos.gtu.edu.tr/wp-content/uploads/2018/03/GTU_sponsor-1.png","1510,1511,1512",0,123);
       dbHandler.insertJob(job);
    }

}
