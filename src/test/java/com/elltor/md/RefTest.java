package com.elltor.md;

import com.elltor.md.util.MdKiller;
import org.junit.Test;

/**
 * @author liuqichun03
 * Created on 2022/11/20
 */
public class RefTest {
    @Test
    public void refEmbedTest() {
        String md = MdKiller.of()
                .title("引用格式嵌套")
                .ref()
                    .text("第一层", MdKiller.Style.BLUE)
                    .ref()
                        .text("第二层", MdKiller.Style.YELLOW)
                        .ref()
                            .text("第三层", MdKiller.Style.RED)
                            .ref()
                                .text("无限套娃 :-) ", MdKiller.Style.GREEN)
                            .endRef()
                        .endRef()
                    .endRef()
                .endRef()
                .build();
        System.out.println(md);
    }
}
