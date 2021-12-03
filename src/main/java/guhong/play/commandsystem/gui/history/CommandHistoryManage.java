package guhong.play.commandsystem.gui.history;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.util.file.JsonFileUtil;
import lombok.Getter;

import java.util.List;

/**
 * 命令历史管理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@SuppressWarnings("all")
public class CommandHistoryManage{

    private static final String HISTORY_LIST_PATH = Constant.DATA_PATH + "/history.json";


    /***
     * 历史列表
     */
    @Getter
    private final List<String> historyList = CollectionUtil.newArrayList();

    /**
     * 历史命令索引
     */
    private volatile Integer index = 0;

    private final Integer maxHistory;


    public CommandHistoryManage() {
        maxHistory = CommandManager.getSystemConfig().getMaxHistory();

        JsonFileUtil.createArrayFile(HISTORY_LIST_PATH);
        List<String> lastData = JsonFileUtil.readClassArray(HISTORY_LIST_PATH, String.class);
        if (CollectionUtil.isNotEmpty(lastData)) {
            historyList.addAll(lastData);
            flush();
        }

    }

    /**
     * 记录历史命令
     * @param str 命令
     */
    public synchronized void add(String str) {
        // 增加命令历史时，总是追加一个空格。这样方便使用历史命令时刻意快速配合命令参数的使用
        this.historyList.add(str.trim() + " ");
        index = this.historyList.size() - 1;

        if (this.historyList.size() > maxHistory) {
            this.historyList.remove(0);
        }

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


    /**
     * 索引加一，并返回
     */
    public synchronized Integer increase() {
        if (index + 1 > this.historyList.size()) {
            return index;
        }
        ++index;
//        print();

        return index;
    }

    /**
     * 索引减一，并返回
     */
    public synchronized Integer decrease() {
        if (index > 0) {
            --index;
        }
//        print();

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

    /**
     * 持久化
     */
    public void save() {
        String jsonString = JSONObject.toJSONString(historyList);
        JsonFileUtil.coveredWriting(HISTORY_LIST_PATH, jsonString);
    }

    private void print() {
        System.out.println("\n命令历史：———————————————————————" + this.getIndex() );
        for (int i = 0; i < this.historyList.size(); i++) {
            System.out.println(i + " : " + this.historyList.get(i) + " : " + (this.getIndex() == i ? "<" : "" ));
        }
    }
}
