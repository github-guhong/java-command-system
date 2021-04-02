package guhong.play.commandsystem.gui.history;

import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 命令历史管理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class CommandHistoryManage{


    /***
     * 历史列表
     */
    private volatile List<String> historyList = CollectionUtil.newArrayList();

    /**
     * 历史命令索引
     */
    private volatile Integer index = 0;

    /**
     * 历史列表最大容量
     */
    private volatile int maxSize = 2;

    /**
     * 记录历史命令
     * @param s 命令
     */
    public synchronized void add(String s) {
        // 限制数量，但我不想写
        this.historyList.add(s);
        index = historyList.size() - 1;
    }

    /**
     * 获得历史命令
     */
    public synchronized String get() {
        if (index < 0) {
            return "";
        }
        if (index > this.historyList.size() - 1) {
            return "";
        }
        return this.historyList.get(index);
    }

    public Integer size() {
        return this.historyList.size();
    }


    /**
     * 索引加一，并返回
     */
    public synchronized Integer increase() {
        if (index + 1 > size()) {
            return index;
        }
        ++index;
        print();

        return index;
    }

    /**
     * 索引减一，并返回
     */
    public synchronized Integer decrease() {
        if (index > 0) {
            --index;
        }
        print();

        return index;
    }

    /**
     * 获得当前索引位置
     * @return 返回当前索引位置
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * 下一条历史命令
     * @return 返回下一条历史命令
     */
    public String next() {
        this.increase();
        return this.get();
    }

    /**
     * 上一条历史命令
     * @return 返回上一条历史命令
     */
    public String last() {
        this.decrease();
        return this.get();
    }

    /**
     * 刷新索引位置
     */
    public void flush() {
        index = historyList.size();
    }

    private void print() {
        System.out.println("\n命令历史：———————————————————————" + this.getIndex() );
        for (int i = 0; i < this.historyList.size(); i++) {
            System.out.println(i + " : " + this.historyList.get(i) + " : " + (this.getIndex() == i ? "<" : "" ));
        }
    }
}
