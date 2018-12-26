/* Status -200 relative result zamaninda gelmemis
 * Status -100 tamamlanmis
 * Status  0 islenmeye hazir (0-200 ayni islem.)
 * Status 100 relative beklemede
 * Status 0* relative cevabi update edilmis BRE'ye yollanmayi bekleyen.
 */

package MainProcess;
import DB.DBHandler;
import DB.Job;
import DB.Orchestration;

import java.util.concurrent.TimeUnit;

public class Runner extends Thread {

    /*
    public Orchestration orch;

    public Runner(Orchestration curr) throws Exception{

        orch.setId(curr.getId());
        orch.setOwnerID(curr.getOwnerID());
        orch.setInsertDateTime(curr.getInsertDateTime());
        orch.setStartJobID(curr.getStartJobID());
        orch.setStatus(curr.getStatus());
        orch.setUpdateDateTime(curr.getUpdateDateTime());

    }

    public void run() {
        try {

            TimeUnit.SECONDS.sleep(300);//5 dk sonra hala status 100 ise cikart.

            DBHandler tmp = new DBHandler();
            Job checker = tmp.getJob(orch.getStartJobID());

            if(checker.getStatus() == 100) {

                //GUI ye ekranlari kapattir.
                orch.setStatus(-200);
                MainProcess.finalUpdate(orch);
            }
        }
        catch(Exception e) {

        }
    }
*/
}
