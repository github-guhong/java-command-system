package guhong.play.commandsystem.job.other.md;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.other.md.service.BeautifyService;
import guhong.play.commandsystem.job.other.md.service.BeautifyServiceContainer;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Getter;

import java.io.File;
import java.util.List;

/**
 * MarkDown美化任务
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class MarkDownBeautifulJob implements CommandJob {

    @Getter
    private BeautifyServiceContainer beautifyServiceContainer = null;

    @Override
    public CommandConfig getCommandConfig() {
        CommandConfig commandConfig = new CommandConfig("mdb");
        commandConfig.setGroup("other");
        commandConfig.setDescription("美化Markdown文件");
        commandConfig.setFileIntroduce("mdb.txt");
        commandConfig.putParamConfig("-b", true);
        commandConfig.putParamConfig("-c", false);
        commandConfig.putParamConfig("-r", true);
        commandConfig.putParamConfig("-l", false);
        return commandConfig;
    }

    @Override
    public void init() {
        beautifyServiceContainer = Singleton.get(BeautifyServiceContainer.class);
    }

    @Override
    public void run(Command command) {
        if (command.isExistParam("-l")) {
            PrintUtil.printWithNumberByName(beautifyServiceContainer.getBeautifyServiceList());
            return;
        }
        String markdownPath = command.getFirstValue();
        if (StrUtil.isBlank(markdownPath)) {
            return;
        }
        File markdownFile = new File(markdownPath);
        File rootFile = markdownFile;
        if (!markdownFile.exists()) {
            PrintUtil.errorPrint("文件地址不存在！");
            return;
        }
        if (markdownFile.isFile()) {
            String rootPath = command.getParamValue("-r");
            if (StrUtil.isNotBlank(rootPath)) {
                rootFile = new File(rootPath);
                if (!rootFile.exists()) {
                    PrintUtil.errorPrint("你设置的根路径不存在！");
                    return;
                }
            }
        }
        String backupPath = command.getParamValue("-b");
        if (null != backupPath) {
            String backFileName = markdownFile.getName() + "." + System.currentTimeMillis() + ".备份文件";
            File backFile = null;
            if (StrUtil.isNotBlank(backupPath)) {
                backFile = new File(backupPath, backFileName);
            } else {
                backFile = new File(markdownFile.getParent(), backFileName);
            }
            FileUtil.copy(markdownFile, backFile, true);
            PrintUtil.println("备份完成！");
        }
        boolean isCover = StrUtil.isNotBlank(command.getParamValue("-c"));
        List<BeautifyService> beautifyServiceList = beautifyServiceContainer.choiceBeautifyService(command.getSecondValue());
        if (CollectionUtil.isEmpty(beautifyServiceList)) {
            return ;
        }
        PrintUtil.println("————————————开始markdown的美化————————————");
        PrintUtil.println("\n");

        for (BeautifyService beautifyService : beautifyServiceList) {
            String serviceName = beautifyService.getName();
            PrintUtil.println("——————开始执行【" + serviceName + "】美化——————");
            startBeautify(markdownFile, rootFile, isCover, beautifyService);
            PrintUtil.println("——————【" + serviceName + "】美化完成——————");
            PrintUtil.println();
        }

        PrintUtil.println();
        PrintUtil.println("————————————全部美化已完成————————————");
    }


    private static void startBeautify(File markdownFile, File rootFile, boolean isCover, BeautifyService beautifyService) {
        if (markdownFile.isDirectory()) {
            File[] files = markdownFile.listFiles();
            if (ArrayUtil.isEmpty(files)) {
                return;
            }
            for (File file : files) {
                startBeautify(file, rootFile, isCover, beautifyService);
            }
        } else {
            String suffix = FileNameUtil.getSuffix(markdownFile);
            if (!"md".equals(suffix)) {
                return;
            }
            PrintUtil.println("————开始对【" + markdownFile.getName() + "】文件进行美化————");
            beautifyService.beautify(markdownFile, rootFile, isCover);
            PrintUtil.println("————【" + markdownFile.getName() + "】美化完成————");
            PrintUtil.println();

        }
    }

}
