package tcpforward;

import util.NetworkUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private ServerSocket serverSocket;
    public HashMap<String, Credentials> clientMap;

    Server() {
        clientMap = new HashMap<>();
        try {
            serverSocket = new ServerSocket(33333);
            while (true) {
                System.out.println("Server waiting");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client accepted");
                serve(clientSocket);
                System.out.println("Client served");
            }
        } catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }

    public void serve(Socket clientSocket) throws IOException, ClassNotFoundException {
        NetworkUtil networkUtil = new NetworkUtil(clientSocket);
        var any = networkUtil.read();
        if (any instanceof Authenticate && ((Authenticate) any).isRegistration){
            System.out.println("Talking from serve");
            String username = ((Authenticate) any).getUsername();
            String password = ((Authenticate) any).getPassword();
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            clientMap.put(username, new Credentials(password, networkUtil));
        }
        new ReadThreadServer(clientMap, networkUtil);
    }

    public static void main(String args[]) {
        Server server = new Server();
    }
}
