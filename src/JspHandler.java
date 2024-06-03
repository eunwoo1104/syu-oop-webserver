import http.*;
import java.io.*;

public class JspHandler {
    private String getClassName(String file) {
        String[] temp = file.split("/");
        return "." + temp[temp.length - 1].split("\\.")[0];
    }

    public void send(OutputStream os, String host, String file, String[] params) throws IOException {
        file = "jsp" + getClassName(file);
        System.out.println("Sending JSP: " + file);
        try {
            Class c = Class.forName(file);
            JspService svc = (JspService) c.newInstance();
            sendText(os, getMsg(200, svc.getHtml(Session.getInstance(), host, params)));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendText(os, getMsg(500, getHtml("서버 오류가 발생하였습니다.")));
        }
    }

    private String getMsg(int code, String html) {
        String msg = "HTTP/1.1 " + code + "\n";
        msg += "Content-Type: text/html;charset=utf-8\n";
        msg += "Content-Language: ko\n";
        msg += "\n";
        msg += html;
        return msg;
    }

    private String getHtml(String desc) {
        return "<html><meta charset='utf-8'>" + desc + "</html>\n";
    }

    private void sendText(OutputStream os, String msg) {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
            pw.println(msg);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getParamValue(String[] params, String name) {
        for (String param : params) {
            String[] toks = param.split("=");
            if (toks.length == 2 && toks[0].equals(name)) return toks[1];
        }
        return null;
    }
}