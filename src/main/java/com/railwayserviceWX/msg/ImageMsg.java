package com.railwayserviceWX.msg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 图片消息
 *
 * @author lid
 * @date 2017.2.22
 */
public class ImageMsg extends Msg {

    //位0x0001被标志时，星标刚收到的消息。
    private String funcFlag;
    // 图片消息媒体id
    private String mediaId;

    /**
     * 开发者调用
     */
    public ImageMsg() {
        this.head = new MsgHead();
        this.head.setMsgType(Msg.MSG_TYPE_IMAGE);//设置消息类型
    }

    /**
     * 程序内部调用
     */
    public ImageMsg(MsgHead head) {
        this.head = head;
    }

    @Override
    public void write(Document document) {
        Element root = document.createElement(WXXmlElementName.ROOT);
        head.write(root, document);

        Element mediaIdElement = document.createElement(WXXmlElementName.MEDIAID);
        mediaIdElement.setTextContent(this.mediaId);

        Element imageElement = document.createElement(WXXmlElementName.IMAGE);
        imageElement.appendChild(mediaIdElement);

        Element funcFlagElement = document.createElement(WXXmlElementName.FUNC_FLAG);
        funcFlagElement.setTextContent(this.funcFlag);

        root.appendChild(imageElement);
        root.appendChild(funcFlagElement);
        document.appendChild(root);
    }

    @Override
    public void read(Document document) {
        this.mediaId = getElementContent(document, WXXmlElementName.MEDIAID);
    }

    public String getFuncFlag() {
        return funcFlag;
    }

    public void setFuncFlag(String funcFlag) {
        this.funcFlag = funcFlag;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

}
