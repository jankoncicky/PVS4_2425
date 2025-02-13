package threads.servers.multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiEchoServer {

    public static void main(String[] args) {
        int port = 8000;

        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server zacina poslouchat na : " + port);

            while(true){
                Socket client = serverSocket.accept();
                System.out.println("Připojil se novy klient: " + client.getInetAddress());

                ClientHandler handler = new ClientHandler(client);
                handler.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Server ukoncen");
    }
}
class ClientHandler extends Thread{
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
            String received;

            while ((received = in.readLine()) != null){
                System.out.println(clientSocket.getInetAddress() +
                        ":"+ clientSocket.getPort() + " posílá: " + received);
                if ("quit".equalsIgnoreCase(received)){
                    System.out.println(clientSocket.getInetAddress() +
                            ":"+ clientSocket.getPort() + " uzavírá komunikaci");
                    break;
                }
                pw.println("Echo: " + received);
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