package com.railwayserviceWX.exception;

public class WeixinMenuOutOfBoundException extends WeixinException {

    private static final long serialVersionUID = -4603326923736279068L;

    public WeixinMenuOutOfBoundException() {
        super("按键数量不能超过3个");
    }
}
