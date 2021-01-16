package guhong.play.commandsystem.gui.command;

/**
 * 命令上下文对象
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface CommandContent {

    /**
     * 追加
     *
     * @param str 命令字符串
     */
    public void append(String str);

    /**
     * 删除一个字符串
     */
    public void delete();

    /**
     * 清空命令
     */
    public void clear();


    /**
     * 获得命令字符串
     *
     * @return 返回命令字符串
     */
    public String getCommandStr();
}
