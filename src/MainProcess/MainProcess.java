package MainProcess;

import DB.*;
import Services.StatusCodes;

import java.util.Arrays;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.apache.commons.lang3.time.StopWatch;

public class MainProcess {
    private static DBHandler dbHandler = new DBHandler();

    private static final int _5min = 300000;

    private static void work(Job job) throws IOException {
        System.out.println(String.format("Job: %d islendi", job.getId())); // job islendi.
        String result = "Hello, world, from " + job.getOwner();
        String fileName=null;
        String fileUrl = job.getFileUrl();
        String[] destinations = job.getDestination().split(",");
        if(job.getDestination().length()>2){
            URL url = new URL(fileUrl);
            fileName =  fileUrl.substring(fileUrl.lastIndexOf('/')+1, fileUrl.length());
            InputStream in = url.openStream();
            Files.copy(in, Paths.get("IncomingFiles\\"+fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        for (String dest :
                destinations) {
            String ftpUrl = "ftp://%s:%s@%s/%s";
            String host = dest + ":21" ;
            String user = "demo";
            String pass = "123";
            String filePath = Paths.get("IncomingFiles\\"+fileName).toString();
            System.out.println("local file url:" + filePath);

            ftpUrl = String.format(ftpUrl, user, pass, host, fileName);
            System.out.println("Upload URL: " + ftpUrl);

            try {
                URL url = new URL(ftpUrl);
                URLConnection conn = url.openConnection();
                OutputStream outputStream = conn.getOutputStream();
                FileInputStream inputStream = new FileInputStream(filePath);

                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

                System.out.println("File uploaded");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static char callBRE(Rule rule) {
      /*  String relativeResults = rule.getRelativeResults();
        List<String> resList = Arrays.asList(relativeResults.split("\\s*,\\s*"));
        if (resList.contains("X"))
            return 'X';
        if (resList.contains("F"))
            return 'F';
            */
        return 'T';
    }

    private static void orchestrationRun(Orchestration orchestration) {
        try {
            // StartJobID orchestration objesinden alinir.
            int currentJobID = orchestration.getStartJobID();

            // Baslangic jobu Db'den cekilir.
            Job currentJob = dbHandler.getJob(currentJobID);
            boolean abnormalState = false;

            while (currentJob.getRuleId() != 0) {

                // Jobun ruleu alinir.
                Rule ruleOfCurrentJob = dbHandler.getRule(currentJob.getRuleId());
                char responseOfBRE;

                StopWatch sw = StopWatch.createStarted();
                while ((responseOfBRE = callBRE(ruleOfCurrentJob)) == 'X' && sw.getTime() < _5min) {
                    ruleOfCurrentJob = dbHandler.getRule(currentJob.getRuleId());
                }
                sw.stop();
                if (responseOfBRE == 'T') {
                    work(currentJob);
                    dbHandler.updateJob(currentJobID, "Status", StatusCodes.SUCCESS);
                    currentJobID = ruleOfCurrentJob.getYesEdge();
                    if (currentJobID == 0) {
                        abnormalState = true;
                        break;
                    }
                    currentJob = dbHandler.getJob(currentJobID);
                } else {
                    dbHandler.updateJob(currentJobID, "Status", StatusCodes.ERROR);
                    currentJobID = ruleOfCurrentJob.getNoEdge();
                    if (currentJobID == 0) {
                        abnormalState = true;
                        break;
                    }
                    currentJob = dbHandler.getJob(currentJobID);
                }
            }
            // Eger en son joba kadar varilirsa, o job da islenir.
            if (!abnormalState) {
                work(currentJob);
                dbHandler.updateOrchestration(orchestration.getId(), "Status", StatusCodes.SUCCESS);  //TODO ?
                dbHandler.updateJob(currentJobID, "Status", 100);                     //TODO ?
            } else {
                dbHandler.updateOrchestration(orchestration.getId(), "Status", StatusCodes.ERROR);  //TODO ?
            }
        } catch (Exception e) {
            // TODO: log basacak.
            System.out.println(String.format("*** An error occured while getting orchestration from DB: %s ***", e));
        }
    }

    public static void main(String[] args) throws Exception {
        Publish.main(null);

       /* while (true) {
            Orchestration orchestration = dbHandler.getOrchestration();
            if (orchestration.getId() != 0) {
                // new thread
                orchestrationRun(orchestration);
            } else {
                System.out.println("No orchestration waiting!");
            }
            Thread.sleep(50);
        }*/
    }
}
