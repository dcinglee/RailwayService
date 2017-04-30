package com.railwayserviceWX.exception;

/**
 * 微信端异常处理类
 *
 * @author lid
 * @date 2017.2.22
 */
public class WeixinException extends Exception {

    private static final long serialVersionUID = 3928879382789269699L;

    public WeixinException(String msg) {
        super(msg);
    }
}
