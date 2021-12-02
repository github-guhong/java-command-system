package guhong.play.commandsystem.job.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 打开文件/目录命令
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class OpenFileJob implements CommandJob {


    private FileIndexContainer fileIndexContainer;


    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        FileCommandConfig commandConfig = new FileCommandConfig("of", "帮你快速打开文件");
        Map<String, Boolean> paramConfig = CollectionUtil.newHashMap();
        paramConfig.put("reload", false);
        paramConfig.put("-i", true);
        paramConfig.put("-s", true);

        paramConfig.put("-d", false);
        paramConfig.put("-f", false);
        paramConfig.put("-e", false);


        paramConfig.put("-il", false);
        paramConfig.put("-sl", false);
        paramConfig.put("-fil", false);

        commandConfig.setFileIntroduce("of.txt");
        commandConfig.setParamConfig(paramConfig);
        return commandConfig;
    }

    @Override
    public void init() {
        fileIndexContainer = Singleton.get(FileIndexContainer.class);
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {

        // 处理各种参数
        String ignoreValue = command.getParamValue("-i");
        if (null != ignoreValue) {
            fileIndexContainer.ignore(ignoreValue);
        }
        String directoryValue = command.getParamValue("-s");
        if (null != directoryValue) {
            fileIndexContainer.addDirectory(directoryValue);
        }
        String reloadValue = command.getParamValue("reload");
        if (null != reloadValue) {
            fileIndexContainer.reload();
        }

        String fileTypeValue = null == command.getParamValue("-f") ? null : "-f";
        String directoryTypeValue = null == command.getParamValue("-d") ? null : "-d";
        FileType fileType = FileType.getFileType(fileTypeValue, directoryTypeValue);
        boolean isEqual = null != command.getParamValue("-e");


        String ilValue = command.getParamValue("-il");
        if (null != ilValue) {
            fileIndexContainer.printIgnoreList();
            return;
        }
        String slValue = command.getParamValue("-sl");
        if (null != slValue) {
            fileIndexContainer.printDirectoryList();
            return;
        }
        String filValue = command.getParamValue("-fil");
        if (null != filValue) {
            fileIndexContainer.printFileIndexList();
            return;
        }

        // 获得文件名，开始查询
        String fileName = command.getFirstValue();
        if (null == fileName) {
            return;
        }
        fileName = fileName.trim();
        PrintUtil.println("\n查找名为[" + fileName + "]的"+ (null == fileType ? "" : fileType.getName()) + "索引");
        List<FileIndexContainer.FileIndex> fileIndexList = fileIndexContainer.get(fileName, fileType, isEqual);
        if (CollectionUtil.isEmpty(fileIndexList)) {
            PrintUtil.println("\n没有找到[" + fileName + "]。如果是新文件请使用[of reload]重新加载。");
            return;
        }

        // 查询到数据打开文件
        String openIndexStr = command.getSecondValue();
        Integer openIndex = null;
        if (null != openIndexStr && ToolUtil.isInteger(openIndexStr)) {
            try {
                openIndex = Integer.parseInt(openIndexStr) - 1;
            } catch (Exception e) {}
        }
        int fileCount = fileIndexList.size();
        if (fileCount == 1) {
            openIndex = 0;
        }
        PrintUtil.println("\n成功找到" + fileCount + "个文件：\n");
        if (fileCount > 1 && StrUtil.isBlank(openIndexStr)) {
            PrintUtil.println("请重新输入命令并指定要打开的文件编号。如：" + command.getSource() +" "+ fileCount + "\n");
        }

        for (int i = 0; i < fileIndexList.size(); i++) {
            FileIndexContainer.FileIndex fileIndex = fileIndexList.get(i);
            String flag = "";
            if (null != openIndex) {
                flag = openIndex == i ? "  <-------- " : "";
            }
            PrintUtil.println((i + 1) + "、" + fileIndex.getFilePath() + flag);
        }
        if (null != openIndex) {
            FileIndexContainer.FileIndex fileIndex = fileIndexList.get(openIndex);
            PrintUtil.println("\n正在打开: " + fileIndex.getFilePath());
            CmdUtil.openFile(fileIndex.toFile());
        }

    }
}
