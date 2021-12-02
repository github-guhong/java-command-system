package guhong.play.commandsystem.job.other.md.service.impl.link.upload;

import cn.hutool.core.io.FileUtil;
import guhong.play.commandsystem.constant.Name;
import guhong.play.commandsystem.job.other.md.service.impl.link.upload.util.UploadUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 上传服务
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface UploadService extends Name {

    /**
     * 上传文件
     * @param filePath 文件地址
     * @return 返回上传后的地址
     */
    public default String upload(String filePath) {
        File file = new File(filePath);
        File downloadFile = null;
        File compressFile = null;
        try {
            // 如果是网络文件就下载到本地
            if (UploadUtil.isNetworkFile(filePath)) {
                downloadFile = UploadUtil.downloadFile(filePath);
                if (null != downloadFile) {
                    file = downloadFile;
                } else {
                    throw new FileNotFoundException("无法从网络上找到：" + file.getPath());
                }
            } else if (!FileUtil.exist(file)) {
                // 如果是本地文件就判断是否存在
                throw new FileNotFoundException("无法从你的电脑上找到：" +file.getPath());
            }

            // 压缩大小
            compressFile = UploadUtil.compressImage(file);
            if (null != compressFile) {
                file = compressFile;
            }

            // 执行上传
            return doUpload(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            FileUtil.del(compressFile);
            FileUtil.del(downloadFile);
            if (UploadUtil.isNetworkFile(filePath)) {
                deleteOld(filePath);
            }
        }
    }

    /**
     * 上传一个本地文件
     * @param localFile 本地文件
     * @return 返回上传后的地址
     * @exception Exception 可能会出现异常
     */
    public String doUpload(File localFile) throws Exception;

    /**
     * 删除数据
     * @param oldPath 旧文件地址
     */
    public void deleteOld(String oldPath);
}
