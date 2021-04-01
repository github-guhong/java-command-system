package guhong.play.commandsystem.job.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.exception.SystemException;
import guhong.play.commandsystem.util.FileOperationUtil;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.io.File;
import java.util.*;

/**
 * 文件索引对象
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class FileIndexManage {


    /**
     * 文件索引
     */
    private List<FileIndex> fileIndex = CollectionUtil.newArrayList();

    /**
     * 忽略列表
     */
    private Set<String> ignoreList = CollectionUtil.newHashSet();

    /**
     * 快捷目录列表
     */
    private Set<String> directoryList = CollectionUtil.newHashSet();

    /**
     * 配置定制
     */
    private String configPath = Constant.CONFIG_PATH + "/file/config.json";

    /**
     * 默认忽略路径
     */
    private String defaultIgnore = ".git,.jpg,.png,.jpeg,.vscode,node_modules";

    public FileIndexManage() {
        JSONObject jsonObject = FileOperationUtil.readConfigAndParse(configPath);
        if (null == jsonObject || CollectionUtil.isEmpty(jsonObject)) {
            return;
        }
        this.fileIndex = jsonObject.getJSONArray("fileIndex").toJavaList(FileIndex.class);
        this.ignoreList = CollectionUtil.newHashSet(jsonObject.getJSONArray("ignoreList").toJavaList(String.class));
        this.directoryList = CollectionUtil.newHashSet(jsonObject.getJSONArray("directoryList").toJavaList(String.class));
    }

    /**
     * 重新加载
     */
    public void reload(String reloadPath) {
//        fileIndex = CollectionUtil.newArrayList();

        Collection<String> reloadPathList = directoryList;
        if (StrUtil.isNotBlank(reloadPath)) {
            reloadPathList = stringToArray(reloadPath);
            checkFilePathExist(reloadPathList);
        }
        if (CollectionUtil.isEmpty(reloadPathList)) {
            return;
        }
        for (String path : reloadPathList) {

            // 遍历指定目
            List<File> files = getAllFile(path);
            // 加入根目录
            files.add(new File(path));
            if (CollectionUtil.isNotEmpty(files)) {
                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    if (isIgnore(file.getPath())) {
                        continue;
                    }

                    // 添加到索引
                    FileIndex fileIndex = new FileIndex(file);
                    fileIndex.print(i+1);
                    this.fileIndex.add(fileIndex);
                }
            } else {
                throw new SystemException("没有找到任何文件，这是不合理的！");
            }
        }

        sync();

    }

    /**
     * 忽略指定文件/目录
     *
     * @param ignoreValue 忽略的值
     */
    public void ignore(String ignoreValue) {
        int oldSize = ignoreList.size();
        this.ignoreList.addAll(stringToArray(ignoreValue));
        int newSize = ignoreList.size();
        if (newSize > oldSize) {
            PrintUtil.println("成功忽略【"+ignoreValue+"】，请使用[of reload]重新构建索引。当前添加的忽略数据有：\n"+this.ignoreList);
        } else {
            PrintUtil.println("该地址以被忽略，不需要重新添加。");
            PrintUtil.println("当前添加的忽略数据有："+this.ignoreList);

        }

    }

    /**
     * 添加目录
     *
     * @param directoryValue 目录地址
     */
    public void addDirectory(String directoryValue) {
        List<String> list = stringToArray(directoryValue);
        // 检查路径是否存在
        checkFilePathExist(list);

        // 检查上级目录是否已存在
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String path = iterator.next();
            int level = 1;
            String parent = FileUtil.getParent(path, level);
            while (null != parent) {
                if (directoryList.contains(parent)) {
                    iterator.remove();
//                    throw new SystemException(path+"这个路径的上层目录已被设置为快捷目录");
                }
                level++;
                parent = FileUtil.getParent(path, level);
            }
        }


        if (CollectionUtil.isNotEmpty(list)) {
            int oldSize = directoryList.size();
            this.directoryList.addAll(list);
            int newSize = directoryList.size();
            if (newSize > oldSize) {
                // 说明有新路径
                PrintUtil.println("成功添加" + (newSize - oldSize) + "个快捷目录,正在自动创建索引。。。");
                this.reload(directoryValue);
                PrintUtil.println("创建成功！");
                return;
            }
        }
        PrintUtil.println("指定的路径以存在，无需重新创建，如需更新索引请使用[of reload "+directoryValue+"]");

    }

    /**
     * 通过文件名获得文件索引信息，可能存在多个
     *
     * @param fileName 文件名
     * @param type 文件类型
     * @param isEqual 是否完全匹配
     * @return 返回索引信息
     */
    public List<FileIndex> get(String fileName, FileType type, boolean isEqual) {
        List<FileIndex> result = CollectionUtil.newArrayList();

        if (CollectionUtil.isEmpty(fileIndex)) {
            PrintUtil.println("\n文件索引不存在，正在尝试重新创建。。。");
            if (CollectionUtil.isEmpty(this.directoryList)) {
                throw new SystemException("没有设置任何快捷目录。你可以通过[of -s 目录名 -i 忽略的文件]来添加快捷目录。详情请使用[help of]查看命令帮助文档");
            }
            this.reload(null);
            if (CollectionUtil.isNotEmpty(fileIndex)) {
                PrintUtil.println("创建完成！");
            } else {
                throw new SystemException("没有找到任何文件，这是不合理的！");
            }
        }
        for (FileIndex index : this.fileIndex) {
            boolean isExist = false;
            if (isEqual) {
                if (index.getFileName().toLowerCase().equals(fileName.toLowerCase())) {
                    isExist = true;
                }
            } else {
                if (index.getFileName().toLowerCase().contains(fileName.toLowerCase())) {
                    isExist = true;
                }
            }

            if (isExist) {
                if (null != type) {
                    String filePath = index.getFilePath();
                    if (type.checkType(filePath)) {
                        result.add(index);
                    }
                } else {
                    result.add(index);
                }
            }

        }
        return result;
    }


    /**
     * 打印文件索引列表
     */
    public void printFileIndexList() {
        for (int i = 0; i < fileIndex.size(); i++) {
            FileIndex index = this.fileIndex.get(i);
            index.print(i+1);
        }
    }

    /**
     * 打印快捷目录地址
     */
    public void printDirectoryList() {
        PrintUtil.println(CollectionUtil.join(directoryList, ","));
    }

    /**
     * 打印忽略的目录地址
     */
    public void printIgnoreList() {
        PrintUtil.println(CollectionUtil.join(ignoreList, ","));
    }


    public List<File> getAllFile(String path) {
        final List<File> fileList = new ArrayList<>();
        return loopFile(fileList, path);
    }

    /**
     * 同步配置
     */
    private void sync() {
        FileOperationUtil.createFile(configPath);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileIndex", fileIndex);
        jsonObject.put("ignoreList", ignoreList);
        jsonObject.put("directoryList", directoryList);
        FileOperationUtil.coverAppendConfig(configPath, jsonObject.toJSONString());
    }


    private List<String> stringToArray(String value) {
        String[] split = value.split(",");
        return Arrays.asList(split);
    }

    private String arrayToString(List<String> array) {
        return ArrayUtil.join(array, ",");
    }


    private void checkFilePathExist(Collection<String> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (String path : list) {
            if (!FileUtil.exist(path)) {
                throw new SystemException("找不到这个路径：" + path);
            }
        }
    }

    /**
     * 是否需要忽略
     *
     * @param path 文件地址
     * @return 忽略返回true
     */
    private boolean isIgnore(String path) {
        if (CollectionUtil.isEmpty(this.ignoreList)) {
            return false;
        }
        for (String ignore : ignoreList) {
            if (ignore.startsWith(".")) {
                // 排除指定后缀
                if (path.endsWith(ignore)) {
                    return true;
                }
            } else if (FileUtil.exist(ignore)) {
                // 排除某一个指定的文件/目录
                if (path.equals(ignore)) {
                    return true;
                }
            } else {
                // 排除所有指定的文件/目录名
                String fileName = new File(path).getName();
                if (fileName.equals(ignore)) {
                    return true;
                }
            }
        }
        return false;
    }


    private List<File> loopFile(List<File> fileList, String path) {
        if (!FileUtil.exist(path)) {
            return fileList;
        }

        File file = new File(path);
        final File[] subFiles = file.listFiles();
        if (ArrayUtil.isNotEmpty(subFiles)) {
            for (File tmp : subFiles) {
                String tmpPath = tmp.getPath();
                if (isIgnore(tmpPath)) {
                    continue;
                }
                fileList.add(tmp);
                loopFile(fileList, tmpPath);
            }
        }
        return fileList;
    }


    @Override
    public String toString() {
        return "FileIndexManage{" +
                "fileIndex=" + fileIndex +
                ", ignoreList=" + ignoreList +
                ", directoryList=" + directoryList +
                '}';
    }

    /**
     * 文件索引
     */
    @Data
    public static class FileIndex {

        private String fileName;

        private String filePath;

        public FileIndex(File file) {
            this.fileName = file.getName();
            this.filePath = file.getPath();
        }

        public FileIndex() {
        }

        public File toFile() {
            return new File(filePath);
        }

        public void print() {
            print(null);
        }

        public void print(Integer i) {
            PrintUtil.println((null == i ? "" : i + ": ") + getFileName() + ": " + getFilePath());
        }
    }
}
