package hu.fourdsoft.memorygame.exception;

public class UserAllreadyExistException extends Exception {
    
    private static final long serialVersionUID = 1L;
    public static final String USER_ALLREADY_EXISTS = "Ez a felhasználónév már létezik!";

    public UserAllreadyExistException() {
        super(USER_ALLREADY_EXISTS);
    }

    public UserAllreadyExistException(String message) {
        super(message);
    }

    public UserAllreadyExistException(Throwable cause) {
        super(cause);
    }

    public UserAllreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAllreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
