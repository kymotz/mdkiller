package com.elltor.md;

import com.elltor.md.util.MdKiller;
import org.junit.Test;

import java.util.LinkedHashMap;

/**
 * @author liuqichun03
 * Created on 2022/11/20
 */
public class SimpleTest {

    @Test
    public void test() {
        String md = MdKiller.of()
                .title("一个标题")
                .text("文本")
                .ref()
                    .text("文本1")
                    .text("文本2")
                    .ul()
                        .text("文本3")
                        .text("文本4")
                    .endUl()
                .endRef()
                .link("链接", "https://elltor.com")
                .build();
        System.out.println(md);
    }

    @Test
    public void normalTest() {
        LinkedHashMap<String, String> urlMappings = new LinkedHashMap<>();
        urlMappings.put("TEAM", "https://elltor.com");
        urlMappings.put("TEAM-Gray", "https://elltor.com");
        urlMappings.put("TEAM-ST", "https://elltor.com");

        String md = MdKiller.of()
                .bigTitle("大标题")
                .title("常规标题")
                .title("指定颜色常规标题", MdKiller.Style.BLUE)
                .title("带标签常规标题", MdKiller.Fonts.of("[P0]", MdKiller.Style.RED), MdKiller.Fonts.of("[P2]",
                        MdKiller.Style.GREEN))
                .subTitle("子标题")
                .text("普通段落、普通文本。普通段落、普通文本。普通段落、普通文本。" +
                        "普通段落、普通文本。普通段落、普通文本。普通段落、普通文本。")
                .subTitle("引用")
                .ref()
                    .text("引用一段文子，或者把引用当作一种排版。引用一段文子，或者把引用当作一种排版。")
                    .br()
                    .link("https://elltor.com")
                .endRef()
                .subTitle("代码块")
                .code()
                    .text("一个代码块")
                    .text("一个代码块")
                    .text("一个代码块")
                .endCode()
                .subTitle("无序列表")
                .ul()
                    .text("无序列表 - 默认")
                    .text("无序列表 - 绿", MdKiller.Style.GREEN)
                    .text("无序列表 - 黄", MdKiller.Style.YELLOW)
                    .text("无序列表 - 蓝", MdKiller.Style.BLUE)
                .endUl()
                .subTitle("有序列表")
                .ol()
                    .text("有序列表 - 粗体", MdKiller.Style.BOLD)
                    .text("有序列表 - 斜体", MdKiller.Style.ITALIC)
                    .text("有序列表 - 加粗、红色字体", MdKiller.Style.RED, MdKiller.Style.BOLD)
                .endOl()
                .subTitle("链接")
                .link("Blog", "https://elltor.com/")
                .links(urlMappings)
                .build();
        System.out.println(md);
    }
}
