package guhong.play.commandsystem.util.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * json配置工具
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class JsonFileUtil {


    public static String read(String path) {
        try {
            if (!FileUtil.exist(path)) {
                PrintUtil.errorPrint("文件不存在！");
                return "";
            }
            return IoUtil.read(FileUtil.getInputStream(path), Charset.defaultCharset());
        } catch (Exception e) {
            PrintUtil.errorPrint(e);
            return "";
        }
    }

    public static JSONObject readJsonObject(String path) {
        String json = read(path);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONObject.parseObject(json);
    }

    public static <T> T readClassObject(String path, Class<T> tClass) {
        String json = read(path);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONObject.parseObject(json, tClass);
    }

    public static JSONArray readJsonArray(String path) {
        String json = read(path);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONObject.parseArray(json);
    }

    public static <T> List<T> readClassArray(String path, Class<T> tClass) {
        String json = read(path);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONObject.parseArray(json, tClass);
    }


    public static void coveredWriting(String path, String content) {
        IoUtil.write(FileUtil.getOutputStream(path), true, content.getBytes());
    }


    public static void createFile(String path, boolean isArray) {
        try {
            if (!FileUtil.exist(path)) {
                FileUtil.mkParentDirs(path);
                File file = new File(path);
                file.createNewFile();
                String content = "{}";
                if (isArray) {
                    content = "[]";
                }
                IoUtil.write(FileUtil.getOutputStream(file), StandardCharsets.UTF_8, true, content);
            }
        } catch (Exception e) {
            PrintUtil.warnPrint(path + "文件创建失败！");
        }
    }

    public static void createArrayFile(String path) {
        createFile(path, true);
    }


    public static void createObjectFile(String path) {
        createFile(path, false);
    }







    }
