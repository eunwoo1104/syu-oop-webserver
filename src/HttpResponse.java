import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponse {
    public void send(OutputStream os, String meth, String host, String file, String[] params) throws IOException {
        if (isJsp(file)) {
            new JspHandler().send(os, host, file, params);
            return;
        }

        file = "web" + file;
        if (file.endsWith("/")) file += "index.html";

        if (!(new File(file)).exists()) {
            System.out.println("File not found: " + file);
            sendText(os, getMsgNotFound());
        } else if (!meth.equals("GET")) {
            sendText(os, getMsgMethodNotAllowed());
        } else {
            System.out.println("Sending file: " + file);
            if (isImage(file)) {
                String ext = getExtension(file);
                byte[] bytes = getMsgImage(file, ext);
                sendBytes(os, bytes);
            } else {
                sendText(os, getMsgText(file));
            }
        }
    }

    private void sendText(OutputStream os, String msg) throws IOException {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
        pw.println(msg);
        pw.flush();
    }

    private void sendBytes(OutputStream os, byte[] bytes) throws IOException {
        os.write(bytes);
        os.flush();
    }

    private String getBaseMsg(int code) {
        String msg = "HTTP/1.1 " + code + "\n";
        msg += "Content-Type: text/html;charset=utf-8\n";
        msg += "Content-Language: ko\n";

        return msg;
    }

    private String getMsgNotFound() {
        String msg = getBaseMsg(404) + "\n";
        msg += "<html>존재하지 않는 파일입니다.</html>";

        return msg;
    }

    private String getMsgMethodNotAllowed() {
        String msg = getBaseMsg(405) + "\n";
        msg += "<html>지원하지 않는 메서드입니다.</html>";

        return msg;
    }

    private String getMsgText(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String msg = getBaseMsg(200);
        msg += "Content-Length: " + bytes.length + "\n";
        msg += "\n";
        msg += new String(bytes, "utf-8");

        return msg;
    }

    private String getExtension(String file) {
        int idx = file.lastIndexOf(".");
        return (idx > 0) ? file.substring(idx + 1) : "";
    }

    private boolean isImage(String file) {
        String ext = getExtension(file);
        return ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("gif") || ext.equals("webp");
    }

    private boolean isJsp(String file) {
        String ext = getExtension(file);
        return ext.equals("jsp");
    }

    private byte[] getMsgImage(String path, String ext) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        String msg = "HTTP/1.1 200\n";
        msg += "Content-Type: image/" + ext + "\n";
        msg += "Content-Length: " + bytes.length + "\n";
        msg += "\n";

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(msg.getBytes());
        bao.write(bytes);
        return bao.toByteArray();
    }
}
