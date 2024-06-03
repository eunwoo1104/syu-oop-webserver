import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket sock;
    public ServerThread(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        try {
            HttpRequest req = HttpRequest.receive(sock.getInputStream());

            if (req.url != null) {
                String host = req.getHost();
                String file = req.getFile();
                String[] params = req.getParams();
                String method = req.getMethod();

                System.out.println("Received from " + host + ": " + method + " " + file);
                HttpResponse res = new HttpResponse();
                res.send(sock.getOutputStream(), method, host, file, params);
            }

            sock.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
