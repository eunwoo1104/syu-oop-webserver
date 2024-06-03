import java.io.*;
import java.net.*;

public class WebServer {
    public void run(int port) {
        try {
            ServerSocket svrsocket = new ServerSocket(port);

            System.out.println("Server started on port " + port + "...\n");
            while (true) {
                Socket sock = svrsocket.accept();
                ServerThread thread = new ServerThread(sock);
                thread.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.run(3000);
    }
}
