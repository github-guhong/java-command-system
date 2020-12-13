package guhong.play.commandsystem.gui.interfaces;

import lombok.Data;

/**
 * 终端接口
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface Terminal {


    /**
     * 终端上打印
     * @param message 打印的信息
     */
    public void print(String message);

    /**
     * 终端上换行打印
     * @param message 打印的消息
     */
    public void println(String message);


    /**
     * 清空打印
     */
    public void clear();
}
