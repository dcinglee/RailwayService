package com.railwayserviceWX.exception;

public class WeixinSubMenuOutOfBoundException extends WeixinException {
    private static final long serialVersionUID = -1745298475620478080L;

    public WeixinSubMenuOutOfBoundException() {
        super("子菜单数量超过5个");
    }
}
