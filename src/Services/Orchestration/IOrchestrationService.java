package Services.Orchestration;

import DB.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;


import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@WebService(name = "OrchestrationService", targetNamespace = "http://orchestration.server.com")
public interface IOrchestrationService {

    /**
     * Introduce an orchestration.
     * @param value Object that contains orchestration information.
     * @param jobs List that contains jobs of orchestration.
     * @param rules List that contains rules of orchestration.
     * @return Message.
     */
    @WebMethod(action = "add_orchestration", operationName = "addOrchestration")
    @WebResult(name = "message")
    String addOrchestration(@WebParam(name = "orchestration") @XmlElement(required = true) Orchestration value,
                            @WebParam(name = "jobList") @XmlElement(required = true) List<Job> jobs,
                            @WebParam(name = "ruleList") @XmlElement(required = true) List<Rule> rules);

}
