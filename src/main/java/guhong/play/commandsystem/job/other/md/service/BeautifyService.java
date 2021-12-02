package guhong.play.commandsystem.job.other.md.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import guhong.play.commandsystem.constant.Name;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 美化服务
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface BeautifyService extends Name {



    /**
     * 美化一个markdown
     * @param markdownFile markdown 文件地址
     * @param rootFile markdown根目录
     * @param isCover 如果之前存在美化后的效果，是否覆盖。true表示覆盖
     */
    public void beautify(File markdownFile, File rootFile, boolean isCover);

    /**
     * 获得markdown文件的内容
     * @param markdownFile markdown文件
     * @return 返回内容
     */
    public default String readMarkdownContent(File markdownFile) {
        if (markdownFile.isDirectory()) {
            return "";
        }
        return IoUtil.read(FileUtil.getReader(markdownFile, StandardCharsets.UTF_8));
    }

    /**
     * 覆盖markdown原文本内容
     * @param markdownFile markdown文件
     * @param newMarkdownContent 新内容
     */
    public default void writeMarkdownContent(File markdownFile, String newMarkdownContent) {
        IoUtil.write(FileUtil.getOutputStream(markdownFile), true, newMarkdownContent.getBytes(StandardCharsets.UTF_8));
    }
}

