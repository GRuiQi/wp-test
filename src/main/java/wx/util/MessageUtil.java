package wx.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import wx.domain.TextMessage;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageUtil {

    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "location";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";

    /**
     * xml数据格式转为集合
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String,String> xmlToMap(HttpServletRequest request)
            throws IOException, DocumentException {
        HashMap<String,String> map = new HashMap<>();
        SAXReader reader = new SAXReader();

        ServletInputStream ins = request.getInputStream();
        Document doc = reader.read(ins);
        Element root = doc.getRootElement();
        List<Element> elementList = root.elements();
        for (Element e :elementList)
            map.put(e.getName(),e.getText());
        ins.close();
        return map;

    }

    /**
     * 对象类型转为xml
     * @param textMessage
     */
    public static String textMessageToXml(TextMessage textMessage){
       XStream xstream = new XStream(new DomDriver("UTF-8"));
       xstream.alias("xml",textMessage.getClass());
       return xstream.toXML(textMessage);
    }


    public static String initText(String toUserName,String fromUserName,String content){
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
        textMessage.setCreateTime(String.valueOf(new Date().getTime()));
        textMessage.setContent(content);
        return MessageUtil.textMessageToXml(textMessage);
    }

    /**
     * 主菜单
     * @return
     */
    public static String menuText(){
        StringBuffer sb = new StringBuffer();
        sb.append("欢迎您的关注，请按照菜单的提示进行操作: \n\n");
        sb.append("1.课程介绍\n");
        sb.append("2.注意事项\n\n");
        sb.append("回复?调出子菜单。");
        return sb.toString();
    }

    public static String fisrtMenu(){
        StringBuffer sb = new StringBuffer();
        sb.append("本课程是Capstone课程，Capstone课程按IEET认证要求是教师不能讲授课程，" +
                "学生自学，教师引导\n");
        return sb.toString();
    }

    public static String secondMenu(){
        StringBuffer sb = new StringBuffer();
        sb.append("基于微信公众号开发");
        return sb.toString();
    }
}
