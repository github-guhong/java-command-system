package guhong.play.commandsystem.job.other.md.service.impl.head;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.job.other.md.service.BeautifyService;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.List;

/**
 * 头部信息美化服务
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class HeadBeautifyService implements BeautifyService {


    /**
     * 获得名字
     *
     * @return 返回服务名字
     */
    @Override
    public String getName() {
        return "头部信息美化服务";
    }

    /**
     * 美化一个markdown
     *
     * @param markdownFile markdown 文件地址
     * @param rootFile markdown根目录
     * @param isCover      如果之前存在美化后的效果，是否覆盖。true表示覆盖
     */
    @Override
    public void beautify(File markdownFile, File rootFile, boolean isCover) {
        HeadInfo headInfo = buildHeadInfo(markdownFile, rootFile);
        if (null == headInfo) {
            return;
        }
        if (doBeautify(headInfo, markdownFile, isCover)) {
            PrintUtil.println(markdownFile.getName() + " 美化成功！");
        }
    }


    private HeadInfo buildHeadInfo(File markdownFile, File rootFile) {
        try {
            String title = markdownFile.getName();
            title = title.substring(0, title.lastIndexOf("."));
            Date createTime = DateUtil.date(getFileCreateTime(markdownFile));
            List<String> parents = getParents(markdownFile, rootFile);
            HeadInfo headInfo = new HeadInfo();
            headInfo.setTitle(title).setCreateTime(createTime).setTags(parents).setCategories(parents);
            return headInfo;
        } catch (Exception e) {
            PrintUtil.errorPrint("美化头部信息失败：获取markdown数据失败：" + e.getMessage());
        }
        return null;
    }

    private boolean doBeautify(HeadInfo headInfo, File markdownFile, boolean isCover) {

        try {
            String markdownContent = readMarkdownContent(markdownFile);
            if (StrUtil.isBlank(markdownContent)) {
                return true;
            }
            if (!isCover) {
                if (isExistHead(markdownContent)) {
                    return true;
                }
            } else {
                // 这样匹配可能会导致错误替换。比如
                /**
                 * ---
                 * title: xx
                 * ---
                 * 正文
                 * ---
                 */
                // 像这样就可能会把正文给覆盖
                if (isExistHead(markdownContent)) {
                    // 拆分为头和尾是因为字符串很大时，调用replaceAll可能会导致栈溢出
                    // 500是因为，我相信在前500字符内一定包含了你的头部信息
                    String headText = StrUtil.sub(markdownContent, 0, 500);
                    String endText = StrUtil.sub(markdownContent,500, markdownContent.length());

                    headText = headText.replaceAll("---(.|\\n|\\r)*---","");
                    markdownContent = headText + endText;
                }

            }
            markdownContent = headInfo.toString() + "\n" + markdownContent;
            writeMarkdownContent(markdownFile, markdownContent);
            return true;
        } catch (Exception e) {
            PrintUtil.errorPrint("头部信息美化失败。"+ e.getMessage());
        }
        return false;

    }


    private  List<String> getParents(File currentFile, File rootFile) {
        List<String> result = CollectionUtil.newArrayList();
        // 把根目录名也算进去
        if (rootFile.isDirectory()) {
            result.add(rootFile.getName());
        } else {
            return result;
        }
        String currentFilePath = currentFile.getParentFile().getPath();
        String path = currentFilePath.substring(rootFile.getPath().length());
        // 因为File.separator的结果是 \ ，但在正则中单独一个 \ 是需要转移的。所以再拼接一个 \ 。
        // 在idea中会警告，在实际运行中不会有错。
        // 在非windows系统中可能会出现问题
        String[] split = path.split("\\" + File.separator);
        for (String s : split) {
            if (StrUtil.isNotBlank(s)) {
                result.add(s);
            }
        }
        return result;
    }

    private Long getFileCreateTime(File file){
        try {
            Path path= Paths.get(file.getPath());
            BasicFileAttributeView basicview= Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
            BasicFileAttributes attr = basicview.readAttributes();
            return attr.creationTime().toMillis();
        } catch (Exception e) {
            e.printStackTrace();
            return file.lastModified();
        }
    }

    private boolean isExistHead(String markdownText) {
        if ("---".equalsIgnoreCase(markdownText.trim().substring(0, 3))) {
            return true;
        }
        return false;
    }
}
