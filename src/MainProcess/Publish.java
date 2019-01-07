package MainProcess;

import DB.DBHandler;
import Services.Approve.ApproveService;
import Services.InfoService.InfoService;
import Services.Orchestration.OrchestrationService;

import javax.xml.ws.Endpoint;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Publish {

    private static String getHostIp(){
        String ip="";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    private static void startOrchestrationService() {
        Object implementor = new OrchestrationService();
        String address = "http://" + getHostIp() + ":9001/OrchestrationService";
        Endpoint.publish(address, implementor);
    }

    private static void startInfoService() {
        Object implementor = new InfoService();
        String address = "http://" + getHostIp() + ":9001/InfoService";
        Endpoint.publish(address, implementor);
    }

    private static void startApproveService() {
        Object implementor = new ApproveService();
        String address = "http://" + getHostIp() + ":9001/ApproveService";
        Endpoint.publish(address, implementor);
    }

    public static void main(String[] argv) {
        startInfoService();
        startOrchestrationService();
        startApproveService();
    }

}
