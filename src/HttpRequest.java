import java.io.*;

public class HttpRequest {
    private String[] msgs;
    public String url=null;
    public HttpRequest(String msg) {
        this.msgs = msg.trim().split("\n");
        this.url = this.getUrl();
    }

    public static HttpRequest receive(InputStream is) throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int cnt;
        while((cnt = is.read(buf)) != -1) {
            bao.write(buf);
            if (cnt < buf.length) break;
        }
        HttpRequest req = new HttpRequest(bao.toString());
        return req;
    }

    public String getUrl() {
        if (this.msgs.length == 0) return null;
        if (this.url != null) return this.url;

        String[] toks = this.msgs[0].trim().split(" ");
        if (toks.length < 2) return null;
        // if (!toks[0].equals("GET") && !toks[0].equals("POST")) return null;

        return toks[1].trim();
    }

    public String getHost() {
        if (this.msgs.length <= 1) return null;

        for(String msg : this.msgs) {
            if (!msg.startsWith("Host") && !msg.startsWith("host")) continue;
            String[] toks = msg.trim().split(":");
            if (toks.length < 2) return null;
            String[] host = new String[toks.length-1];
            for(int i=1; i<toks.length; i++) host[i-1] = (toks[i].trim());
            return String.join(":", host);
        }

        return null;
    }

    public String getFile() {
        return this.url.split("\\?")[0];
    }

    public String[] getParams() {
        if (!this.url.contains("?")) return null;
        String[] param = this.url.split("\\?");
        if (param.length < 2) return null;
        return param[1].split("&");
    }

    public String getMethod() {
        String[] toks = this.msgs[0].trim().split(" ");
        if (toks.length < 2) return null;
        return toks[0];
    }
}
