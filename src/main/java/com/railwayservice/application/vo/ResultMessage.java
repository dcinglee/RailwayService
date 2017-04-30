package com.railwayservice.application.vo;

/**
 * 统一返回消息的格式，其JSON用于接口格式。
 *
 * @author Ewing
 */
public class ResultMessage {
    // 当success不能满足需要时使用code
    private int code = 0;
    private boolean success = false;
    private String message = "";
    // 返回数据可以是任意对象
    private Object data = "";

    public ResultMessage() {
    }

    public ResultMessage(int code, boolean success, String message) {
        this.code = code;
        this.success = success;
        this.message = message;
    }

    // 快速创建消息的方法。

    public static ResultMessage newSuccess() {
        return new ResultMessage(1, true, "操作成功！");
    }

    public static ResultMessage newSuccess(String message) {
        if (message == null) message = "操作成功！";
        return new ResultMessage(1, true, message);
    }

    public static ResultMessage newFailure() {
        return new ResultMessage(2, false, "操作失败！");
    }

    public static ResultMessage newFailure(String message) {
        if (message == null) message = "操作失败！";
        return new ResultMessage(2, false, message);
    }

    public ResultMessage toSuccess() {
        this.code = 1;
        this.success = true;
        this.message = "操作成功！";
        return this;
    }

    public ResultMessage toFailure() {
        this.code = 2;
        this.success = false;
        this.message = "操作失败！";
        return this;
    }

    public int getCode() {
        return code;
    }

    public ResultMessage setCode(int code) {
        this.code = code;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResultMessage setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public <T> T getData() {
        return (T) data;
    }

    public ResultMessage setData(Object data) {
        this.data = data;
        return this;
    }
}
