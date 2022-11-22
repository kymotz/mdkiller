package com.elltor.md;

import com.elltor.md.util.MdKiller;
import org.junit.Test;

/**
 * @author liuqichun03
 * Created on 2022/11/15
 */
public class ListTest {
    @Test
    public void codeEmbedTest() {
        String md = MdKiller.of()
                .title("嵌套代码块")
                .code()
                    .text("这是一个代码块")
                    .text("这是一个代码块")
                    .text("这是一个代码块")
                .endCode()
                .ref()
                    .code()
                        .text("这是一个代码块")
                        .text("这是一个代码块")
                        .text("这是一个代码块")
                    .endCode()
                .endRef()
                .build();
        System.out.println(md);
    }

    @Test
    public void listEmbedTest1() {
        String md = MdKiller.of()
                .title("嵌套列表-初级 :)")
                .ref()
                    .ol()
                        .text("简单的有序列表")
                        .text("简单的有序列表")
                    .endOl()
                .endRef()
                .ref()
                    .ul()
                    .text("简单的无序列表")
                    .text("简单的无序列表")
                    .endUl()
                .endRef()
                .build();
        System.out.println(md);
    }

    @Test
    public void listEmbedTest2() {
        String md = MdKiller.of()
                .title("嵌套列表-同类型嵌套-中级 :-)")
                .ref()
                    .ol()
                        .text("一级")
                        .text("一级")
                        .ol()
                            .text("二级")
                            .text("二级")
                            .ol()
                                .text("三级")
                            .endOl()
                            .text("二级")
                        .endOl()
                        .text("一级")
                    .endOl()
                .endRef()
                .ref()
                    .ul()
                    .text("一级")
                    .text("一级")
                    .ul()
                        .text("二级")
                        .text("二级")
                        .ul()
                            .text("三级")
                        .endUl()
                        .text("二级")
                    .endUl()
                    .text("一级")
                    .endUl()
                .endRef()
                .build();
        System.out.println(md);
    }

    @Test
    public void listEmbedTest3() {
        String md = MdKiller.of()
                .title("嵌套列表-不同类型交叉嵌套-高级 :O ")
                .ref()
                    .ol()
                        .text("第一类")
                            .ul()
                                .text("class 1")
                                .text("class 2")
                                .text("class 3")
                                .ol()
                                    .text("type 1")
                                    .text("type 2")
                                    .text("type 3")
                                .endOl()
                            .endUl()
                        .text("第二类")
                        .ol()
                            .text("first")
                            .text("second")
                            .text("third")
                            .ul()
                                .text("第 3.1")
                                .text("第 3.2")
                                .text("第 3.3")
                            .endUl()
                            .text("forth")
                        .endOl()
                        .text("第三类")
                    .endOl()
                .endRef()
                .build();
        System.out.println(md);
    }
}
