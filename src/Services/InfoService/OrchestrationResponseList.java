
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name ="OrchestrationResponseList", namespace="OrchestrationList")
public class OrchestrationResponseList {
    
    private ArrayList<OrchestrationResponse> orcList;
    private int ownerID;

    public ArrayList<OrchestrationResponse> getOrcList() {
        return orcList;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOrcList(ArrayList<OrchestrationResponse> orcList) {
        this.orcList = orcList;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
    
    
    
    
}
