package Services.InfoService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name ="ServerToGUI", namespace="Server")
public class JobResponse {

    protected Integer jobId;
    protected Integer jobOwner;
    protected String jobDescription;
    protected String relatives;
    protected Integer status;

    public Integer getJobId() {
        return jobId;
    }

    public Integer getJobOwner() {
        return jobOwner;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getRelatives() {
        return relatives;
    }

    public Integer getStatus() {
        return status;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public void setJobOwner(Integer jobOwner) {
        this.jobOwner = jobOwner;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setRelatives(String relatives) {
        this.relatives = relatives;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    
}
