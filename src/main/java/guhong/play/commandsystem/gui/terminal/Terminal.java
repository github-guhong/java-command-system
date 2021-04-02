package guhong.play.commandsystem.gui.terminal;

import cn.hutool.core.lang.Singleton;
import guhong.play.commandsystem.gui.command.CommandContent;
import guhong.play.commandsystem.gui.command.DefaultCommandContent;
import guhong.play.commandsystem.gui.history.CommandHistoryManage;
import guhong.play.commandsystem.gui.key.KeyListenerHandlerManage;

/**
 * 终端接口
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface Terminal {


    /**
     * 终端上打印
     *
     * @param message 打印的信息
     */
    public void print(String message);

    /**
     * 终端上换行打印
     *
     * @param message 打印的消息
     */
    public void println(String message);

    /**
     * 清空打印
     */
    public void clear();

    /**
     * 获得终端上的文本
     */
    public String getText();

    /**
     * 在终端上设置文本
     * @param text 文本
     */
    public void setText(String text);

    /**
     * 获得键盘监听处理器管理器
     *
     * @return 返回键盘监听处理器列表
     */
    public default KeyListenerHandlerManage getKeyListenerHandlerManage() {
        return Singleton.get(KeyListenerHandlerManage.class);
    }


    /**
     * 获得命令上下文
     *
     * @return 返回命令上下文
     */
    public default CommandContent getCommandContent() {
        return Singleton.get(DefaultCommandContent.class);
    }

    /**
     * 获得历史命令管理器
     * @return 返回历史列表
     */
    public default CommandHistoryManage getHistoryCommandManage() {
        return Singleton.get(CommandHistoryManage.class);
    }



}
