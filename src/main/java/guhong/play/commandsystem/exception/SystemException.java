package guhong.play.commandsystem.exception;

import lombok.Data;

/**
 * 系统异常
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class SystemException extends RuntimeException {


    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SystemException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "系统异常：" + super.getMessage();
    }
}
