package Services.Approve;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MyRequestApprove", namespace="http://your.namespace.com")
public class requestApprove implements Serializable{

    @XmlElement(name = "userApprove", required = true)
    protected String userApprove;
    @XmlElement(name = "jobId", required = true)
    protected int jobId;
    @XmlElement(name = "relativeId", required = true)
    protected  int relativeId;

    public String getUserApprove() {
        return userApprove;
    }

    public int getJobId() {
        return jobId;
    }

    public int getRelativeId() {
        return relativeId;
    }
}