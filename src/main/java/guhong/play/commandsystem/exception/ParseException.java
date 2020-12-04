package guhong.play.commandsystem.exception;

/**
 * 解析错误
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class ParseException extends RuntimeException {


    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ParseException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "命令解析错误: "+super.getMessage();
    }
}
