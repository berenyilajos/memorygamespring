package hu.fourdsoft.memorygame.exception;

import java.io.Serializable;

public class MyApplicationException extends Exception implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public MyApplicationException() {
    }

    public MyApplicationException(String message) {
        super(message);
    }

    public MyApplicationException(Throwable cause) {
        super(cause);
    }

    public MyApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
