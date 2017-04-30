package com.railwayserviceWX.msg;

import org.w3c.dom.Document;

/**
 * @author lid
 * @date 2017.2.22
 */
public class VoiceMsg extends Msg {

    // 语音消息媒体id，可以调用多媒体文件下载接口拉取该媒体
    private String mediaId;
    // 语音格式：amr
    private String format;
    // 语音识别结果，UTF8编码
    private String recognition;
    // 消息id，64位整型
    private String msgId;

    public VoiceMsg(MsgHead head) {
        this.head = head;
    }

    @Override
    public void write(Document document) {
    }

    @Override
    public void read(Document document) {
        this.mediaId = getElementContent(document, WXXmlElementName.MEDIAID);
        this.format = getElementContent(document, WXXmlElementName.FORMAT);
        this.recognition = getElementContent(document, WXXmlElementName.RECOGNITION);
        this.msgId = getElementContent(document, WXXmlElementName.MSG_ID);
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

}
