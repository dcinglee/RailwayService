package com.railwayserviceWX.util;

import com.railwayserviceWX.msg.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的Session实现
 * 使用默认Session实现类需要通过添加HandleMessageListener监听器来对微信消息进行监听
 * 可以添加多个监听器来处理不同的消息内容。
 *
 * @author lid
 */

public class DefaultSession extends Session {

    /**
     * 监听器集合
     */
    private List<HandleMessageListener> listeners = new ArrayList<HandleMessageListener>(3);

    /**
     * 私有构造方法
     */
    private DefaultSession() {
    }

    /**
     * 创建一个Session实例
     */
    public static DefaultSession newInstance() {
        return new DefaultSession();
    }

    /**
     * 添加监听器
     *
     * @param handleMassge
     */
    public void addOnHandleMessageListener(HandleMessageListener handleMassge) {
        listeners.add(handleMassge);
    }

    /**
     * 移除监听器
     */
    public void removeOnHandleMessageListener(HandleMessageListener handleMassge) {
        listeners.remove(handleMassge);
    }

    @Override
    public void onTextMsg(TextMsg msg) {
        for (HandleMessageListener currentListener : listeners) {
            currentListener.onTextMsg(msg);
        }
    }

    @Override
    public void onImageMsg(ImageMsg msg) {
        for (HandleMessageListener currentListener : listeners) {
            currentListener.onImageMsg(msg);
        }
    }

    @Override
    public void onEventMsg(EventMsg msg) {
        for (HandleMessageListener currentListener : listeners) {
            currentListener.onEventMsg(msg);
        }
    }

    @Override
    public void onLinkMsg(LinkMsg msg) {
        for (HandleMessageListener currentListener : listeners) {
            currentListener.onLinkMsg(msg);
        }
    }

    @Override
    public void onLocationMsg(LocationMsg msg) {
        for (HandleMessageListener currentListener : listeners) {
            currentListener.onLocationMsg(msg);
        }
    }

    @Override
    public void onErrorMsg(int errorCode) {
        for (HandleMessageListener currentListener : listeners) {
            currentListener.onErrorMsg(errorCode);
        }
    }

    @Override
    public void onVoiceMsg(VoiceMsg msg) {
        for (HandleMessageListener currentListener : listeners) {
            currentListener.onVoiceMsg(msg);
        }
    }

    @Override
    public void onVideoMsg(VideoMsg msg) {
        for (HandleMessageListener currentListener : listeners) {
            currentListener.onVideoMsg(msg);
        }
    }
}
