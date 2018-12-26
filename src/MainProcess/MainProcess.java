package MainProcess;

import DB.*;

public class MainProcess {
    private static DBHandler dbHandler = new DBHandler();

    public static void main(String[] args) {
        Publish.main(null);
/*
        try {
            Orchestration orchestration = dbHandler.getOrchestration();
//            System.out.println(orchestration.getStatus());
            if (orchestration.getId() != 0) {
                int currentJobID = orchestration.getStartJobID();
                Job currentJob = dbHandler.getJob(currentJobID);
                System.out.println(currentJobID);
            }
        } catch (Exception e) {
            // TODO: log basacak.
            System.out.println(String.format("*** An error occured while getting orchestration from DB: %s ***", e));
        }
*/
    }

  /*

    public static void finalUpdate(Orchestration curr) throws Exception {
        DBHandler tmp = new DBHandler();
        tmp.updateOrchestration(curr.getId(), "Status", curr.getStatus());
        tmp.updateOrchestration(curr.getId(), "StartingJobId", curr.getStartJobID());//Orchestration i kuculterek gidiyoruz.
    }

    public static void statusUpdate(Job curr) throws Exception {
        DBHandler tmp = new DBHandler();
        tmp.updateJob(curr.getId(), "Status" , curr.getStatus());
    }

    public static char toBRE (int ruleID, String ruleQuery, String currRelatives) throws Exception{
        //parametreler BRE ye yollanilcak ?
        return 'X';
    }

    public static void main(String[] args) throws Exception{
        mainProcess();
    }

    public static void sendFile(Job sent) {
        //1. demodaki yollama?
    }

    private static void mainProcess() throws Exception{

        int check = 0;


        DBHandler tmp = new DBHandler();
        Orchestration newOrch = tmp.getOrchestration();
        // while(newOrch == null) {

        //}

        Job currJob = tmp.getJob(newOrch.getStartJobID());
        Rule currRule = tmp.getRule(currJob.getRuleId());

        while(check == 0) {
            char result = ' ';
            result = toBRE( currRule.getId()  , currRule.getQuery() , currRule.getRelativeResults() );

            if(result == 'X') {
                newOrch.setStatus(100);
                finalUpdate(newOrch);//Beklemeyi db ye isledik.
                Runner thread1= new Runner(newOrch);
                thread1.start();
                while(newOrch == null) {
                    newOrch = tmp.getOrchestration();
                }
                currJob = tmp.getJob(newOrch.getStartJobID());
                currRule = tmp.getRule(currJob.getRuleId());
            } else if(result == 'T') {
                sendFile(currJob);
                currJob.setStatus(-100);//Job bitti.
                statusUpdate(currJob);
                //GUIye ekranlari kapattir.

                if(currRule.getYesEdge() != 0) {//0 End node icin.
                    currJob = tmp.getJob(currRule.getYesEdge());
                    newOrch.setStartJobID(currJob.getId());
                    newOrch.setStatus(0);
                    finalUpdate(newOrch);//Orchestrationi daralttik.
                    statusUpdate(currJob);//True edgedeki jobu hazirladik.
                } else {
                    newOrch.setStatus(-100);//Bittigini db ye yazdik.
                    finalUpdate(newOrch);

                    newOrch=null;
                    //yeni is alma
                    while(newOrch == null) {
                        newOrch = tmp.getOrchestration();
                    }

                    currJob = tmp.getJob(newOrch.getStartJobID());
                    currRule = tmp.getRule(currJob.getRuleId());
                }
            } else if(result == 'F') {
                //GUIye ekranlari kapattir
                if(currRule.getNoEdge() != 0) {//0 = END EDGE
                    currJob = tmp.getJob(currRule.getNoEdge());
                    newOrch.setStartJobID(currJob.getId());
                    newOrch.setStatus(0);
                    finalUpdate(newOrch);//Orchestrationi daralttik.
                    statusUpdate(currJob);//True edgedeki jobu hazirladik.

                }
                else {//job bittiyse statusu -100 e cektik.
                    newOrch.setStatus(-100);
                    finalUpdate(newOrch);

                    newOrch=null;
                    //yeni is alcaz
                    while(newOrch == null) {
                        newOrch = tmp.getOrchestration();
                    }

                    currJob = tmp.getJob(newOrch.getStartJobID());
                    currRule = tmp.getRule(currJob.getRuleId());
                }
            }
        }
    }

    */
}
