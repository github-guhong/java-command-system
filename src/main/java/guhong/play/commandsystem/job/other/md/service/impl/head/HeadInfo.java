package guhong.play.commandsystem.job.other.md.service.impl.head;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 头信息
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@Accessors(chain = true)
public class HeadInfo {

    private String title;

    private Set<String> tags = CollectionUtil.newHashSet();

    private List<String> categories = CollectionUtil.newArrayList();

    public HeadInfo setTags(Collection<String> tags) {
        this.tags.addAll(tags);
        return this;
    }

    public HeadInfo setCategories(List<String> categories) {
        this.categories.addAll(categories);
        return this;
    }

    private Date createTime;

    @Override
    public String toString() {
        String tagsString = arrayToString(tags);
        String categoriesString = arrayToString(categories);
        return "---\n" +
                "title: "+title+"\n" +
                "tags:\n" + tagsString +
                "categories:\n" + categoriesString +
                "date: " + DateUtil.dateNew(createTime).toMsStr() + "\n" +
                "---";
    }

    private String arrayToString(Collection<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            if (StrUtil.isBlank(s)) {
                continue;
            }
            sb.append("- ").append(s).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
       String str = "123456";
       System.out.println(StrUtil.sub(str, 0, 8) + StrUtil.sub(str,4, str.length()));
    }
}
