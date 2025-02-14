package threads.servers.multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobQueueServer {
    private static final int PORT = 11111;
    public static final List<String> clientOutputs =
            Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("Startuji server na " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Pripojil se klient z portu: " + clientSocket.getPort());

                JobHandler handler = new JobHandler(clientSocket);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
class JobHandler extends Thread{
    private Socket clientSocket;

    public JobHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String received;
            boolean err;
            String sub;

            while ((received = in.readLine()) != null){
                err = true;
                if(received.trim().startsWith("push")){
                    sub = received.substring(received.indexOf("push") + 5);
                    JobQueueServer.clientOutputs.add(sub);
                    err = false;
                    out.println("Pridano do listu: " + sub);
                }
                if ("quit".equalsIgnoreCase(received)){
                    System.out.println(clientSocket.getInetAddress() +
                            ":"+ clientSocket.getPort() + " uzavírá komunikaci");
                    break;
                }
                if ("list".equalsIgnoreCase(received)){
                    out.println(JobQueueServer.clientOutputs);
                    err = false;
                }
                if(err){
                    out.println("Neznámý command");
                }

            }
            System.out.println("Klient: " + clientSocket.getInetAddress() + ":"
                    + clientSocket.getPort() + " se odpojil.");
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
