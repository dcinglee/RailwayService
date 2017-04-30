package com.railwayservice.application.exception;

/**
 * 应用程序异常类，可以传递数据给调用者。
 * 也可用于将编译期异常转换成运行时异常。
 *
 * @author Ewing
 */
public class AppException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2658311251759056124L;
	private int type = 0;
    private Object data;

    // 方便的构造方法，按需调用。

    public AppException() {
    }

    public AppException(String massage) {
        super(massage);
    }

    public AppException(String massage, int type) {
        super(massage);
        this.type = type;
    }

    public AppException(String massage, int type, Object data) {
        super(massage);
        this.type = type;
        this.data = data;
    }

    // 支持链式调用的Setter和Getter。

    public int getType() {
        return type;
    }

    public AppException setType(int type) {
        this.type = type;
        return this;
    }

    // 泛型自动转换
    @SuppressWarnings("unchecked")
	public <T> T getData() {
        return (T) data;
    }

    public AppException setData(Object data) {
        this.data = data;
        return this;
    }
}
