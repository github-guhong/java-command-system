package guhong.play.commandsystem.job.other.md.service.impl.link;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 图片路径信息
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class LinkPathInfo {

    /**
     * 链接id
     */
    private String linkId;

    /**
     * 绝对路径
     */
    private String absolutePath;

    /**
     * 源路径
     */
    private String sourcePath;

    public String getAbsolutePath() {
        if (StrUtil.isBlank(absolutePath)) {
            return sourcePath;
        }
        return absolutePath;
    }


    @Override
    public String toString() {
        return getAbsolutePath();
    }
}
