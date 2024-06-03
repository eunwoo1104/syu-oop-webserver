package http;

public interface JspService {
    public String getHtml(Session ses, String host, String[] params);
}