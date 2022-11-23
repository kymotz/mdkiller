package com.elltor.md;

import com.elltor.md.util.MdKiller;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author liuqichun03
 * Created on 2022/11/20
 */
public class BestPractice {

    /**
     * 链式调用 vs 普通调用
     */
    @Test
    public void callByChainShow() {
        String md = MdKiller.of()
                .title("标题")
                .text("文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落")
                .ref()
                    .text("引用中的普通文本")
                    .text("引用中的普通文本-设置颜色", MdKiller.Style.RED)
                    .text("引用中的普通文本-加粗", MdKiller.Style.BOLD)
                    .text("名字", Arrays.asList("值1", "值2", "值3","值4", "值5"))
                .endRef()
                .link("有问题点链接", "https://elltor.com")
                .build();
        System.out.println(md);
    }

    @Test
    public void callByNormalShow() {
        MdKiller.SectionBuilder bd = MdKiller.of();
        bd.title("标题");
        bd.text("文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落");
        // 进入块级元素返回新 builder 对象，需要对象接收
        bd = bd.ref();
        bd.text("引用中的普通文本");
        bd.text("引用中的普通文本-设置颜色", MdKiller.Style.RED);
        bd.text("引用中的普通文本-加粗", MdKiller.Style.BOLD);
        bd.text("名字", Arrays.asList("值1", "值2", "值3","值4", "值5"));
        // 出块级元素返回父 builder 对象，需要对象接收
        bd = bd.endRef();
        bd.link("有问题点链接", "https://elltor.com");
        String md = bd.build();
        System.out.println(md);
    }

    /**
     * 一个美观的消息模版，通过标题、引用中的文本和超链接形成了一个较为有格式的排版
     */
    @Test
    public void aBeautifulMsgTemplate() {
        String md = MdKiller.of()
                .title("标题")
                .text("文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落")
                .ref()
                    .text("引用中的普通文本")
                    .text("引用中的普通文本-设置颜色", MdKiller.Style.RED)
                    .text("引用中的普通文本-加粗", MdKiller.Style.BOLD)
                    .text("") // 一个空行
                    .text("名字", Arrays.asList("值1", "值2", "值3","值4", "值5"))
                .endRef()
                .link("有问题点链接", "https://elltor.com")
                .build();
        System.out.println(md);
    }

    /**
     * 通过标题、列表、超链接形成排版
     */
    @Test
    public void aBeautifulMsgTemplate2() {
        String md = MdKiller.of()
                .title("标题")
                .text("文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落")
                .ul()
                    .text("引用中的普通文本")
                    .text("引用中的普通文本-设置颜色", MdKiller.Style.RED)
                    .text("引用中的普通文本-加粗", MdKiller.Style.BOLD)
                    .text("名字", Arrays.asList("值1", "值2", "值3","值4", "值5"))
                .endUl()
                .link("有问题点链接", "https://elltor.com")
                .build();
        System.out.println(md);
    }

    /**
     * 可以直接放置表格，或者在引用块中嵌套表格
     */
    @Test
    public void tableShow() {
        String[][] data = {
                {"姓名",  "姓别",  "芳龄", "身高"},
                {"不知火舞",null,   "18", "173"},
                {"孙策",  "男",    "23", "181"},
                {"李白",  "男",    null, "179"},
        };

        String md = MdKiller.of()
                .title("使用表格")
                .text("月老的记事本：", MdKiller.Style.RED)
                .table()
                    .data(data)
                .endTable()
                .subTitle("嵌套表格")
                .ref()
                    .text("月老的记事本：", MdKiller.Style.YELLOW)
                    .table()
                        .data(data)
                    .endTable()
                .endRef()
                .build();
        System.out.println(md);
    }

    /**
     * 通过工具生成一个消息模版，你可以通过缓存消息模版进一步提高性能
     */
    @Test
    public void genTemplateAndCacheShow() {
        String mdTemplate = MdKiller.of()
                .title("%s")
                .text("%s")
                .ref()
                    .text("%s")
                .endRef()
                .link("%s", "%s")
                .build();
        String realMd = String.format(mdTemplate,
                "消息模版标题",
                "这是一个消息模版，你可以通过缓存消息模版以提高性能。",
                "在引用中的消息 —— 这是一个消息模版，你可以通过缓存消息模版以提高性能。",
                "详情链接🔗", "https://elltor.com");
        System.out.println(realMd);
    }

    /**
     * 灵活在文本中拼接 Markdown 样式
     */
    @Test
    public void joinMarkdownShow() {
        String md = MdKiller.of()
                .text("### 标题")
                .ref()
                    .text("**文本**")
                    .text("名字", "*值(斜体)*")
                    .text("加粗文本", MdKiller.Style.BOLD)
                    .link("链接", "https://elltor.com")
                .endRef()
                .text("[详情链接🔗](https://elltor.com)")
                .build();
        System.out.println(md);
    }
}
