package com.railwayserviceWX.util;

import com.railwayserviceWX.msg.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

/**
 * 抽象会话
 * 此会话声明周期在一个请求响应内。
 * 通过继承类实现各种微信消息的处理方法
 *
 * @author lid
 * @date 2017.2.22
 */
public abstract class Session {
    /**
     * 时间格式化
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    /**
     * Document构建类
     */
    private static DocumentBuilder builder;
    private static TransformerFactory tffactory;

    static {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        //格式化工厂对象
        tffactory = TransformerFactory.newInstance();
    }

    //输入流
    private InputStream is;
    //输出流
    private OutputStream os;

    public Session() {
    }

    /**
     * 解析微信消息，并传递给对应方法
     *
     * @param is 输入流
     * @param os 输出流
     */
    public void process(InputStream is, OutputStream os) {
        this.os = os;
        try {
            Document document = builder.parse(is);
            MsgHead head = new MsgHead();
            head.read(document);
            String type = head.getMsgType();

            if (Msg.MSG_TYPE_TEXT.equals(type)) {//文本消息
                TextMsg msg = new TextMsg(head);
                msg.read(document);
                this.onTextMsg(msg);
            } else if (Msg.MSG_TYPE_IMAGE.equals(type)) {//图片消息
                ImageMsg msg = new ImageMsg(head);
                msg.read(document);
                this.onImageMsg(msg);
            } else if (Msg.MSG_TYPE_EVENT.equals(type)) {//事件推送
                EventMsg msg = new EventMsg(head);
                msg.read(document);
                this.onEventMsg(msg);
            } else if (Msg.MSG_TYPE_LINK.equals(type)) {//链接消息
                LinkMsg msg = new LinkMsg(head);
                msg.read(document);
                this.onLinkMsg(msg);
            } else if (Msg.MSG_TYPE_LOCATION.equals(type)) {//地理位置消息
                LocationMsg msg = new LocationMsg(head);
                msg.read(document);
                this.onLocationMsg(msg);
            } else if (Msg.MSG_TYPE_VOICE.equals(type)) {
                VoiceMsg msg = new VoiceMsg(head);
                msg.read(document);
                this.onVoiceMsg(msg);
            } else if (Msg.MSG_TYPE_VIDEO.equals(type)) {
                VideoMsg msg = new VideoMsg(head);
                msg.read(document);
                this.onVideoMsg(msg);
            } else {
                this.onErrorMsg(-1);//这里暂时这样处理的
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回传消息给微信服务器
     * 只能再接收到微信服务器消息后，才能调用此方法
     *
     * @param msg 消息对象（支持：文本、音乐、图文）
     */
    public void callback(Msg msg) {
        Document document = builder.newDocument();
        msg.write(document);
        try {
            Transformer transformer = tffactory.newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(new OutputStreamWriter(os, "utf-8")));
        } catch (Exception e) {
            e.printStackTrace();// 保存dom至目输出流
        }
    }

    /**
     * 关闭
     */
    public void close() {
        try {
            if (is != null) {
                System.out.println("is != null");
                is.close();
            }
            if (os != null) {
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到文本消息
     *
     * @param msg
     */
    public abstract void onTextMsg(TextMsg msg);

    /**
     * 收到图片消息
     *
     * @param msg
     */
    public abstract void onImageMsg(ImageMsg msg);

    /**
     * 收到事件推送消息
     *
     * @param msg
     */
    public abstract void onEventMsg(EventMsg msg);

    /**
     * 收到链接消息
     *
     * @param msg
     */
    public abstract void onLinkMsg(LinkMsg msg);

    /**
     * 收到地理位置消息
     *
     * @param msg
     */
    public abstract void onLocationMsg(LocationMsg msg);

    /**
     * 收到语音识别消息
     *
     * @param msg
     */
    public abstract void onVoiceMsg(VoiceMsg msg);

    /**
     * 收到视频消息
     *
     * @param msg
     */
    public abstract void onVideoMsg(VideoMsg msg);

    /**
     * 错误消息
     *
     * @param msg
     */
    public abstract void onErrorMsg(int errorCode);
}
