package MainProcess;

import BizTalkLog.Logger.BizLog;
import BizTalkLog.Logger.LogLevel;
import DB.*;
import Services.InfoService.RulesAndJobs;
import Services.StatusCodes;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.sun.jmx.snmp.Timestamp;
import org.apache.commons.lang3.time.StopWatch;

public class MainProcess {
    private static DBHandler dbHandler = new DBHandler();

    public static String createMessageFile(String message) throws IOException {
        Date date = new Date();

        long time = date.getTime();

        long ts = System.currentTimeMillis() / 1000L;


        try (PrintWriter out = new PrintWriter("temp/" + ts + ".message")) {
            out.println(message);
        }
        return String.valueOf(ts);

    }


    private static void work(Job job) throws IOException {
        System.out.println(String.format("Job: %d islendi", job.getId())); // job islendi.
        String result = "Hello, world, from " + job.getOwner();
        String fileName = null;
        String fileUrl = job.getFileUrl();
        String[] destinations = job.getDestination().split(",");
        String[] messages = job.getDescription().split("~");
        int count = 0;
        fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1, fileUrl.length());

        for (String dest : destinations) {
            dest = dest.replaceAll(" ", "");
            String ftpUrlStart = "ftp://%s:%s@%s/%s";
            String ftpUrl = null;
            String host = dest + ":21";
            String user = "BizTalk";
            String pass = "123";
            // String filePath = Paths.get("IncomingFiles\\"+fileName).toString();
            String filePath = job.getFileUrl();
            System.out.println("local file url:" + filePath);

            ftpUrl = String.format(ftpUrlStart, user, pass, host, fileName);
            System.out.println("Upload URL: " + ftpUrl);

            URL url = new URL(ftpUrl);
            URLConnection conn = url.openConnection();
            OutputStream outputStream = conn.getOutputStream();
            //FileInputStream inputStream = new FileInputStream(filePath);
            InputStream inputStream = new URL(filePath).openStream();


            //Send main file
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);

            }


            //Send message info
            String messageFile = createMessageFile(messages[count]);
            String messagePath = Paths.get("temp\\" + messageFile + ".message").toString();
            System.out.println("local file url:" + messagePath);

            String ftpUrlMessage = String.format(ftpUrlStart, user, pass, host, fileName + ".message");
            System.out.println("Upload URL: " + ftpUrlMessage);
            url = new URL(ftpUrlMessage);
            conn = url.openConnection();
            OutputStream outputStreamMessage = conn.getOutputStream();
            FileInputStream inputStreamMessage = new FileInputStream(messagePath);
            buffer = new byte[4096];

            while ((bytesRead = inputStreamMessage.read(buffer)) != -1) {

                outputStreamMessage.write(buffer, 0, bytesRead);

            }
            inputStreamMessage.close();
            outputStreamMessage.close();
            inputStream.close();
            outputStream.close();

            File deleteFile = new File(messagePath);
            if (deleteFile.delete()) {
                System.out.println("tmp/file.txt File deleted from Project root directory");
            } else System.out.println("File tmp/file.txt doesn't exists in project root directory");

            System.out.println("File uploaded");
            ++count;
        }

    }

    private static char checkRule(Rule rule) {
        String relativeResults = rule.getRelativeResults();
        List<String> resList = Arrays.asList(relativeResults.split("\\s*,\\s*"));
        if (resList.contains("X"))
            return 'X';
        if (resList.contains("F"))
            return 'F';
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
                //char responseOfBRE = checkRule(ruleOfCurrentJob);
                char responseOfBRE = 'T';

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
               // dbHandler.updateOrchestration(orchestration.getId(), "Status", StatusCodes.SUCCESS);  //TODO ?
                dbHandler.updateJob(currentJobID, "Status", 100);                     //TODO ?
             //   orchFinishLog(orchestration);
            } else {
                dbHandler.updateOrchestration(orchestration.getId(), "Status", StatusCodes.ERROR);  //TODO ?
            }
        } catch (Exception e) {
            // TODO: log basacak.
            System.out.println(String.format("*** An error occured while getting orchestration from DB: %s ***", e));
        }
    }

    private static void orchFinishLog(Orchestration orchestration) throws Exception {
        DBHandler dbHandler = new DBHandler();
        ArrayList<RulesAndJobs> rulesAndJobs = dbHandler.getRulesAndJobs(orchestration.getId());
        System.out.println(rulesAndJobs);
/*        ArrayList<Job> jobList = rulesAndJobs.get(0).getJobs();
        ArrayList<Rule> ruleList = rulesAndJobs.get(0).getRules();*/
        /*for (int i = 0; i < jobList.size(); ++i) {
            Job job = jobList.get(i);
            for (int j = 0; j < ruleList.size(); ++j) {
                Rule rule = ruleList.get(j);
                if (job.getRuleId() == rule.getId()) {
                    BizLog.Log("1", String.valueOf(orchestration.getOwnerID()), LogLevel.INFO,
                            job, rule, orchestration);
                    break;
                }
            }
        }
        dbHandler.updateOrchestration(orchestration.getId(), "Status", StatusCodes.SUCCESS);  //TODO ?
        BizLog.Log("1", String.valueOf(orchestration.getOwnerID()), LogLevel.INFO,orchestration);*/
    }

    public static Runnable singleJobExecution() {
        //DBHandler dbHandlerSingle = new DBHandler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Job job = null;
                    try {
                        job = dbHandler.getJob();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (job.getId() != 0) {
                        // new thread
                        try {
                            work(job);
                            dbHandler.updateJob(job.getId(), "Status", StatusCodes.SUCCESS);//TODO ?

                        } catch (Exception e) {
                            try {
                                dbHandler.updateJob(job.getId(), "Status", StatusCodes.ERROR);//TODO ?
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("No single job waiting!");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        return runnable;
    }

    public static Runnable orchestrationExecution() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Orchestration orchestration = null;
                    try {
                        orchestration = dbHandler.getOrchestration();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (orchestration.getId() != 0) {
                        // new thread
                        try {
                            MainProcess.orchFinishLog(orchestration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //          orchestrationRun(orchestration);
                    } else {
                        //System.out.println("No orchestration waiting!");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return runnable;
    }

    public static void main(String[] args) throws Exception {
        try {
            Thread jobThread = new Thread(singleJobExecution());
            Thread orchThread = new Thread(orchestrationExecution());

            orchThread.start();
            //jobThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
          Publish.main(null);


    }
}
