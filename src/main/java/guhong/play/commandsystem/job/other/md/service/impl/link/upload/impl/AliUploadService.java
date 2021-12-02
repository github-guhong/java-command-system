package guhong.play.commandsystem.job.other.md.service.impl.link.upload.impl;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.dto.entity.SystemConfig;
import guhong.play.commandsystem.exception.ThirdPartyException;
import guhong.play.commandsystem.job.other.md.service.impl.link.upload.UploadService;
import guhong.play.commandsystem.util.ToolUtil;
import lombok.Data;
import lombok.NonNull;

import java.io.File;

/**
 * 阿里云OSS服务
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class AliUploadService implements UploadService {


    /**
     * key
     */
    private String accessKeyId;

    /**
     * 密码
     */
    private String accessKeySecret;

    /**
     * 所在地域
     */
    private String endpoint;

    /**
     * 桶名字
     */
    private String bucketName;
    /**
     * 前缀
     */
    private String prefix;

    private OSS ossClient = null;

    public AliUploadService() {
        SystemConfig systemConfig = CommandManager.getSystemConfig();
        JSONObject thirdParty = systemConfig.getThirdParty();
        if (null == thirdParty) {
            return;
//            throw new SystemException("没有找到任何第三方的配置，无法加载" + getName());
        }
        JSONObject aliyun = thirdParty.getJSONObject("aliyun");
        if (null == aliyun) {
            return;
//            throw new SystemException("没有找到任何阿里云配置，无法加载" + getName());
        }
        accessKeyId = aliyun.getString("accessKeyId");
        accessKeySecret = aliyun.getString("accessKeySecret");
        JSONObject oss = aliyun.getJSONObject("oss");
        if (null == oss) {
            return;
//            throw new SystemException("没有找到任何阿里云OSS配置，无法加载" + getName());
        }
        endpoint = oss.getString("endpoint");
        bucketName = oss.getString("bucketName");
        prefix = oss.getString("prefix");
        if (ToolUtil.isOneEmpty(endpoint, accessKeyId, accessKeySecret, bucketName, prefix)) {
            return;
//            throw new SystemException("阿里云OSS配置存在空值，无法加载" + getName());
        }
        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);;
    }

    /**
     * 上传一个本地文件
     *
     * @param localFile 本地文件。必须是一个文件才行
     * @return 返回上传后的地址
     * @exception Exception 可能会出现异常
     */
    @Override
    public String doUpload(@NonNull File localFile) throws Exception {
        String suffix = FileNameUtil.getSuffix(localFile);
        if (StrUtil.isBlank(suffix)) {
            suffix = "";
        } else if (StrUtil.isNotBlank(suffix)) {
            suffix = "." + suffix;
        }
        String objectName = prefix + "/" + IdUtil.fastUUID() + suffix;
        getOssClient().putObject(bucketName, objectName, localFile);
        getOssClient().setObjectAcl(bucketName, objectName, CannedAccessControlList.PublicRead);
        return this.getOssFilePath(objectName);
    }

    /**
     * 删除旧数据
     *
     * @param oldPath 旧文件地址
     */
    @Override
    public void deleteOld(String oldPath) {
        // 如果包含bucket地址，说明是阿里oss的数据
        if (oldPath.contains( bucketName + "." + endpoint)) {
            OSS ossClient = getOssClient();
            String name = new File(oldPath).getName();
            String key =  prefix + "/" + name;
            ossClient.deleteObject(bucketName, key);
        }
    }

    @Override
    public String getName() {
        return "阿里云OSS服务";
    }


    /**
     * 获得oss上的文件路径
     * @param objectName 文件名
     */
    private String getOssFilePath(String objectName) {
        return "https" + "://" + bucketName + "." + endpoint + "/" + objectName;
    }

    public OSS getOssClient() {
        if (null == ossClient) {
            throw new ThirdPartyException("阿里云OSS服务未初始化成功，请检查配置。");
        }
        return ossClient;
    }


}
