package guhong.play.commandsystem.gui.history;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 命令历史列表
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class CommandHistoryList extends ArrayList<String> {

    /**
     * 最大容量
     */
    private volatile int maxSize = 2;

    /**
     * Appends the specified element to the end of this list.
     *
     * @param s element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    @Override
    public synchronized boolean add(String s) {
        // 限制数量，但我也不想写
        return super.add(s);
    }
}
