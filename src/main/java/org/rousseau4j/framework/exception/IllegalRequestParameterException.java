package org.rousseau4j.framework.exception;

/**
 * Created by ZhouHangqi on 2017/8/2.
 */
public class IllegalRequestParameterException extends RuntimeException {

    public IllegalRequestParameterException() {
        super();
    }

    public IllegalRequestParameterException(String message) {
        super(message);
    }

    public IllegalRequestParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalRequestParameterException(Throwable cause) {
        super(cause);
    }

    public IllegalRequestParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
