package guhong.play.commandsystem.util;

import guhong.play.commandsystem.exception.SystemException;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * xml工具
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class XmlOperationUtil {

    /**
     * 获取项目maven版本
     * @param projectPath 项目地址
     * @return 返回maven版本
     */
    public static String getMavenVersion(String projectPath) {
        File file = new File(projectPath + "/pom.xml");
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            // 读取XML文件,获得document对象
            document = saxReader.read(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (null == document) {
            throw new SystemException("项目pom文件读取失败，请检查！");
        }
        Element rootElement = document.getRootElement();
        Element version = rootElement.element("version");
        return version.getData().toString();
    }

}
