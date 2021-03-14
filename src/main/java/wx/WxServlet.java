package wx;


import org.dom4j.DocumentException;
import wx.util.CheckUtil;
import wx.util.MessageUtil;
import wx.domain.TextMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;


@WebServlet("/wx")
public class WxServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

       /* signature	微信加密签名，signature结合了开发者填写的token参数和请求中的
                    timestamp参数、nonce参数。
        timestamp	时间戳
        nonce	随机数
        echostr	随机字符串*/

        String signature =  req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");

        PrintWriter out = resp.getWriter();
        if (CheckUtil.checkSignature(signature,timestamp,nonce))
            out.print(echostr);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        try {
            Map<String, String> map = MessageUtil.xmlToMap(req);
            String toUserName = map.get("ToUserName");
            String fromUserName = map.get("FromUserName");
            String createTime = map.get("CreateTime");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
            String msgId = map.get("MsgId");

            String message = null;
            if (MessageUtil.MESSAGE_TEXT.equals(msgType)){
                if("1".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.fisrtMenu());

                }else if("2".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.secondMenu());

                }else if("?".equals(content)||"？".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());

                }

            }else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
                String eventType = map.get("Event");
                //关注后的逻辑处理
                if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());

                }

            }
            //微信使用的是"ISO-8859-1"编码格式
            message = new String(message.getBytes("utf-8"),"ISO-8859-1");
            out.print(message);
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }

    }
}
