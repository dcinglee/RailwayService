package com.railwayserviceWX.util;

import com.railwayserviceWX.msg.*;

/**
 * 主要用于接收微信服务器消息的接口
 *
 * @author lid
 * @date 2017.2.22
 */
public interface HandleMessageListener {
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
     * 语音识别消息
     *
     * @param msg
     */
    public abstract void onVoiceMsg(VoiceMsg msg);

    /**
     * 错误消息
     *
     * @param msg
     */
    public abstract void onErrorMsg(int errorCode);

    /**
     * 视频消息
     *
     * @param msg
     */
    public abstract void onVideoMsg(VideoMsg msg);

}
