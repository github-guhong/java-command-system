package guhong.play.commandsystem.job.other.md.service.impl.link.upload.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 上传相关的工具
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class UploadUtil {

    /**
     * 压缩图片的宽
     */
    private static final int COMPRESS_IMAGE_WIDTH = 500;

    /***
     * 压缩图片的高
     */
    private static final int COMPRESS_IMAGE_HEIGHT = 600;

    /**
     * 进行压缩的范围， kb
     */
    private static final int MAX = 100;

    /**
     * 从网络地址下载一个文件到本地
     * @param networkAddress 网络地址
     * @return 返回本地地址
     */
    @SneakyThrows
    public static File downloadFile(String networkAddress) {
        if (!isNetworkFile(networkAddress)) {
            return null;
        }
        File tempFile = UploadUtil.getTempFile(FileNameUtil.getSuffix(networkAddress));
        URL url = new URL(networkAddress);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = url.openStream();
            outputStream = FileUtil.getOutputStream(tempFile);
            IoUtil.copy(inputStream, outputStream);
            return tempFile;
        } catch (Exception e) {
            return null;
        } finally {
            IoUtil.close(outputStream);
            IoUtil.close(inputStream);
        }

    }


    /**
     * 图片压缩策略枚举
     */
    public enum CompressImageStrategyEnum {
        /**
         * 按大小压缩
         */
        SIZE,
        /**
         * 按质量压缩
         */
        QUALITY
    }

    /**
     * 压缩图片。采用默认策略压缩。
     *
     * @param file 源文件对象
     * @return 返回压缩后的文件对象
     */
    public static File compressImage(File file) {
        return UploadUtil.compressImage(file, CompressImageStrategyEnum.QUALITY);
    }

    /**
     * 根据指定压缩策略压缩图片
     * 如果图片大小小于100kb，则不会压缩图片，直接返回原图片。并且如果你传入的并不是一个图片文件，该方法也不会做任何处理。
     * 该方法会在源文件同目录下创建一个后缀为 _com 的压缩后的文件。
     * 如：
     * sourceFile = D:/javaFileTest/093647.jpg
     * 压缩后
     * compressFile = D:/javaFileTest/093647_com.jpg
     *
     * @param file             源文件
     * @param compressStrategy 压缩策略
     * @return 返回压缩后的对象， 返回null表示没有压缩或者失败
     */
    public static File compressImage(File file, CompressImageStrategyEnum compressStrategy) {

        if (!FileUtil.exist(file)) {
            throw new RuntimeException("sourceFile is null");
        }

        BufferedInputStream inputStream = FileUtil.getInputStream(file);

        // 验证图片大小。如果图片小于100kb，则不压缩。
        try {
            int available = inputStream.available();
            // 转化为kb
            int fileSize = available / 1024;
            if (fileSize < MAX) {
                return null;
            }
            File compressFile = getTempFile("jpg");
            // 开始压缩
            switch (compressStrategy) {
                case QUALITY:
                    // 图片尺寸不变，压缩图片文件大小,outputQuality参数1为最高质量，如果是1，可能比原图还大
                    Thumbnails.of(inputStream).scale(1f).outputQuality(0.25f).toFile(compressFile);
                    break;

                default:
                    // 改变图片尺寸压缩图片
                    Thumbnails.of(inputStream).size(COMPRESS_IMAGE_WIDTH, COMPRESS_IMAGE_HEIGHT).toFile(compressFile);
                    break;
            }
            return compressFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(inputStream);
        }


    }


    /**
     * 获得一个临时文件
     * 记得删除哦
     * @param suffix 临时文件的后缀，不用带“."
     * @return 返回文件地址
     */
    @SneakyThrows
    public static File getTempFile(String suffix) {
        if (StrUtil.isBlank(suffix)) {
            suffix = ".unknown";
        } else {
            suffix = "." + suffix;
        }
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + IdUtil.fastUUID() + suffix);
        if (!file.exists()) {
            FileUtil.mkParentDirs(file);
            file.createNewFile();
        }
        return file;
    }

    public static boolean isNetworkFile(String networkAddress) {
        if (null == networkAddress) {
            return false;
        }
        return networkAddress.startsWith("http");
    }
}
