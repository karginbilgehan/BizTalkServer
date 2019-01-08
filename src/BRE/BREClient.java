package BRE;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

public class BREClient {
    private static final String REST_URI
            = "http://10.1.46.182:8080/rule";
    private static String ruleParameters = "<rule id='215'>" + "<clause>(1,2,3).(2,3,4)</clause>" + "<relatives>1,2,3,4</relatives>" + "</rule>";
    private static final String url
            = "http://10.1.46.182:8080/rule/answer";
    private static String answerParameters = "<response>\n" +
            "    <user_id>41</user_id>\n" +
            "    <rule_id>215</rule_id>\n" +
            "    <answer>t</answer>\n" +
            "</response>\n";

    private static HttpURLConnection conn;

    public static void request(){

        String urlParameters = ruleParameters;

        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try {

            URL myurl = new URL(REST_URI);
            conn = (HttpURLConnection) myurl.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/xml");

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            System.out.println(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            conn.disconnect();
        }
    }

    public static int add(String query, int ruleID, String relatives) {
        return -1;
    }

    public static String approve(int ruleID, int relativeID, String answer) {
        return "T";
    }
}
