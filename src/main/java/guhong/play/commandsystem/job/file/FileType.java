package guhong.play.commandsystem.job.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.function.Function;

/**
 * 文件类型
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public enum FileType  {
    /**
     * 文件
     */
    FILE("-f","文件", new FileCheck() {
        @Override
        public boolean checkType(String filePath) {
            return FileUtil.isFile(filePath);
        }
    }),
    /**
     * 目录
     */
    DIRECTORY("-d", "目录", new FileCheck() {
        @Override
        public boolean checkType(String filePath) {
            return FileUtil.isDirectory(filePath);
        }
    });

    private String value;

    private String name;

    private FileCheck fileCheck;


    FileType(String value, String name, FileCheck fileCheck) {
        this.value = value;
        this.name = name;
        this.fileCheck = fileCheck;
    }

    /**
     * 获得文件的类型
     * @param typeValue 值，可以多个
     * @return 返回文件类型
     */
    public static FileType getFileType(String... typeValue) {
        if (ArrayUtil.isNotEmpty(typeValue)) {
            for (String value : typeValue) {
                if (StrUtil.isNotBlank(value)) {
                    for (FileType fileType : values()) {
                        if (fileType.getValue().equals(value)) {
                            return fileType;
                        }
                    }
                }
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public FileCheck getFileCheck() {
        return fileCheck;
    }

    public String getName() {
        return name;
    }

    public boolean checkType(String filePath) {
        return getFileCheck().checkType(filePath);
    }
}

interface FileCheck {

    /**
     * 检查文件类型
     * @param filePath 文件路径
     * @return 是对应的文件类型则返回true
     */
    public boolean checkType(String filePath);
}

