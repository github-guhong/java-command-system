package guhong.play.commandsystem.job.other.md.service.impl.link;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.job.other.md.service.BeautifyService;
import guhong.play.commandsystem.job.other.md.service.impl.link.upload.UploadService;
import guhong.play.commandsystem.job.other.md.service.impl.link.upload.impl.AliUploadService;
import guhong.play.commandsystem.job.other.md.service.impl.link.upload.util.UploadUtil;
import guhong.play.commandsystem.util.file.JsonFileUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图片美化服务
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class ImageBeautifyService implements BeautifyService {

    private static final String LINK_CACHE_PATH = Constant.CACHE_PATH + "/mdb/link-cache.json";

    private UploadService uploadService = Singleton.get(AliUploadService.class);

    private static final Pattern IMAGE_PATTERN = Pattern.compile("!\\[.*]\\(.*\\)");

    public ImageBeautifyService() {
        try {
            if (!FileUtil.exist(LINK_CACHE_PATH)) {
                FileUtil.mkParentDirs(LINK_CACHE_PATH);
                File file = new File(LINK_CACHE_PATH);
                file.createNewFile();
                String content = "[]";
                IoUtil.write(FileUtil.getOutputStream(file), StandardCharsets.UTF_8, true, content);
            }
        } catch (Exception e) {
            PrintUtil.warnPrint("链接文件缓存创建失败！");
        }
    }

    /**
     * 获得名字
     *
     * @return 返回服务名字
     */
    @Override
    public String getName() {
        return "图片美化服务";
    }


    /**
     * 美化一个markdown
     *
     * @param markdownFile markdown 文件地址
     * @param rootFile     markdown根目录
     * @param isCover      如果之前存在美化后的效果，是否覆盖。true表示覆盖
     */
    @Override
    public void beautify(File markdownFile, File rootFile, boolean isCover) {
        String markdownFileName = markdownFile.getName();
        String markdownContent = readMarkdownContent(markdownFile);
        if (StrUtil.isBlank(markdownContent)) {
            return;
        }
        List<String> imageInfoList = this.getImageInfo(markdownContent);
        if (CollectionUtil.isEmpty(imageInfoList)) {
            PrintUtil.println("没有在【" + markdownFileName + "】中找到图片信息，美化结束。");
            return;
        }
        List<LinkPathInfo> imageFileList = this.parseImageFileList(markdownFile, imageInfoList);
        PrintUtil.println("在【" + markdownFileName + "】中找到以下图片信息：");
        PrintUtil.printLnWithNumber(imageFileList);

        for (int i = 0; i < imageFileList.size(); i++) {
            LinkPathInfo linkPathInfo = imageFileList.get(i);
            String absolutePath = linkPathInfo.getAbsolutePath();
            String sourcePath = linkPathInfo.getSourcePath();
            String linkId = linkPathInfo.getLinkId();

            // http表示已经是美化过的
            if (absolutePath.startsWith("http") && !isCover) {
                PrintUtil.println(absolutePath + "已经美化过了，跳过。");
                continue;
            }
            try {
                String uploadPath = getCache(linkId);
                if (StrUtil.isBlank(uploadPath)) {
                    PrintUtil.println("开始使用【" + uploadService.getName() + "】上传文件。。。");
                    uploadPath = uploadService.upload(absolutePath);
                    PrintUtil.println(absolutePath + "上传成功：" + uploadPath);
                    saveCache(linkId, uploadPath);
                } else {
                    PrintUtil.println(absolutePath + "以前美化过，直接使用缓存 -->" + uploadPath);
                }
                // 替换旧内容
                String oldImageInfo = imageInfoList.get(i);
                String newImageInfo = oldImageInfo.replace(sourcePath, uploadPath);
                markdownContent = markdownContent.replace(oldImageInfo, newImageInfo);
            } catch (Exception e) {
                PrintUtil.errorPrint(absolutePath + "上传失败：" + e.getMessage());
            }
        }

        writeMarkdownContent(markdownFile, markdownContent);
    }

    private void saveCache(String imageId, String uploadPath) {
        if (!FileUtil.exist(LINK_CACHE_PATH)) {
            return;
        }
        JSONArray cacheArray = JsonFileUtil.readJsonArray(LINK_CACHE_PATH);
        if (null == cacheArray) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imageId", imageId);
        jsonObject.put("uploadPath", uploadPath);
        cacheArray.add(jsonObject);
        IoUtil.write(FileUtil.getOutputStream(LINK_CACHE_PATH), StandardCharsets.UTF_8, true, cacheArray.toJSONString());
    }

    private String getCache(String imageId) {
        if (!FileUtil.exist(LINK_CACHE_PATH)) {
            return null;
        }
        JSONArray cacheArray = JsonFileUtil.readJsonArray(LINK_CACHE_PATH);
        if (null == cacheArray) {
            return null;
        }
        for (Object o : cacheArray) {
            JSONObject jsonObject = JSONObject.parseObject(o.toString());
            String cacheImageId = jsonObject.getString("imageId");
            if (StrUtil.isNotBlank(cacheImageId) && cacheImageId.equalsIgnoreCase(imageId)) {
                return jsonObject.getString("uploadPath");
            }
        }
        return null;
    }

    /**
     * 将原始的文件信息转为文件路径
     * 如果是相对路径就转为换绝对路径
     *
     * @param markdownFile 当前在处理的markdown文件
     * @param imageInfo    文件信息
     * @return 返回文件列表
     */
    private List<LinkPathInfo> parseImageFileList(File markdownFile, List<String> imageInfo) {
        List<LinkPathInfo> result = CollectionUtil.newArrayList();
        for (String image : imageInfo) {
            LinkPathInfo linkPathInfo = new LinkPathInfo();
            String imagePath = image.substring(image.indexOf("(") + 1, image.lastIndexOf(")"));
            String linkId = image.substring(image.indexOf("[") + 1, image.lastIndexOf("]"));
            linkPathInfo.setLinkId(linkId);
            linkPathInfo.setSourcePath(imagePath);
            result.add(linkPathInfo);

            // 不是网络地址，不是绝对路径。那肯定是相对路径了
            if (!UploadUtil.isNetworkFile(imagePath) && !FileUtil.isAbsolutePath(imagePath)) {
                String absolutePath = this.getAbsolutePath(markdownFile, imagePath);
                linkPathInfo.setAbsolutePath(absolutePath);
            }
        }
        return result;
    }

    /**
     * 获得图片的绝对路径
     *
     * @param currentFile 当前文件
     * @param imagePath   图片地址
     * @return 返回绝对路径
     */
    private String getAbsolutePath(File currentFile, String imagePath) {
        Matcher matcher = Pattern.compile("\\.\\./").matcher(imagePath);
        // 默认返回一级
        int i = 1;
        while (matcher.find()) {
            ++i;
        }
        File parent = FileUtil.getParent(currentFile, i);
        String substring = File.separator + imagePath.substring(imagePath.lastIndexOf("../") + 3);
        return FileUtil.normalize(parent.getPath() + substring);
    }

    /**
     * 获得markdown中的图片信息
     *
     * @param markdownContent markdown文本内容
     * @return 返回图片信息。如[!xxx](xxx)
     */
    private List<String> getImageInfo(String markdownContent) {
        List<String> result = CollectionUtil.newArrayList();
        Matcher matcher = IMAGE_PATTERN.matcher(markdownContent);
        while (matcher.find()) {
            String group = matcher.group();
            result.add(group);
        }
        return result;
    }


}
