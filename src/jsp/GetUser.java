package jsp;

import http.*;

public class GetUser implements JspService {
    public String getHtml(Session ses, String host, String[] params)
    {
        String[] prms = (String[]) ses.get(host);
        if (prms == null)
            return "<html><meta charset='utf-8'> 회원 정보가 없습니다. </html>";
        return "<html><meta charset='utf-8'> 회원아이디: " + prms[0] + " </html>";
    }
}
