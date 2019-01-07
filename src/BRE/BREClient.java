package BRE;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class BREClient {


    private static final String REST_URI
            = "http://localhost:8082/spring-jersey/resources/employees";

    private Client client = ClientBuilder.newClient();

    public Character getJsonEmployee(String rule, int ruleId, String relatives) {
        return client
                .target(REST_URI)
                .path(String.valueOf(rule))
                .request(MediaType.APPLICATION_XML)
                .get(Character.class);
    }


}
