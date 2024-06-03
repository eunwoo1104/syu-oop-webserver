package jsp;

import http.*;

public class SetUser implements JspService {
    public String getHtml(Session ses, String host, String[] params)
    {
        if (params.length == 0)
            return "<html><meta charset='utf-8'> 추가할 세션 정보가 없습니다. </html>";
        ses.set(host, params);
        return "<html><meta charset='utf-8'> 세션 정보가 추가되었습니다. </html>";
    }
}
