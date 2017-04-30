package com.railwayserviceWX.msg;

import org.w3c.dom.Document;

/**
 * 视频消息
 *
 * @author lid
 * @date 2017.2.22
 */
public class VideoMsg extends Msg {
    // 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
    private String mediaId;
    // 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
    private String thumbMediaId;
    // 消息id，64位整型
    private String msgId;

    /**
     * 开发者调用
     */
    public VideoMsg() {
        //this.head = new MsgHead();
        //this.head.setMsgType(Msg.MSG_TYPE_MUSIC);
    }

    public VideoMsg(MsgHead head) {
        this.head = head;
    }

    @Override
    public void write(Document document) {
    }

    // 因为用户不能发送音乐消息给我们，因此没有实现
    @Override
    public void read(Document document) {
        this.mediaId = getElementContent(document, WXXmlElementName.MEDIAID);
        this.thumbMediaId = getElementContent(document, WXXmlElementName.THUMBMEDIAID);
        this.msgId = getElementContent(document, WXXmlElementName.MSG_ID);
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}