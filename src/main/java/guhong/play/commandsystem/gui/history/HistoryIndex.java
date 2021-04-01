package guhong.play.commandsystem.gui.history;

import lombok.Data;

/**
 * 历史索引
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class HistoryIndex {

    private volatile Integer index = 0;

    /**
     * 索引加一，并返回
     */
    public synchronized Integer increase() {
        ++index;
        return index;
    }

    /**
     * 索引减一，并返回
     */
    public synchronized Integer decrease() {
        if (index > 0) {
            --index;
        }
        return index;
    }

    public Integer getIndex() {
        return index;
    }

    /**
     * 归零
     */
    public void flush() {
        index = 0;
    }
}
