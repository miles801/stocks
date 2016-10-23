package eccrm.security.exception;

/**
 * 在线状态异常
 * Created by Michael on 2014/9/2.
 */
public class OnlineStatusException extends SecurityException {

    public OnlineStatusException() {
        super();
    }

    public OnlineStatusException(String s) {
        super(s);
    }

    public OnlineStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public OnlineStatusException(Throwable cause) {
        super(cause);
    }
}
