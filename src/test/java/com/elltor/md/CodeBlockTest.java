package com.elltor.md;

import com.elltor.md.util.MdKiller;
import org.junit.Test;

/**
 * @author liuqichun03
 * Created on 2022/11/20
 */
public class CodeBlockTest {

    @Test
    public void normalCodeBlockTest() {
        String md = MdKiller.of()
                .title("Code Block Test")
                .code()
                    .text("public static void main(String[][] args) {")
                    .text("    System.out.println(\"hello world\");")
                    .text("}")
                .endCode()
                .ref()
                    .text("Code Block with Ref")
                    .code()
                        .text("public static void main(String[][] args) {")
                        .text("    System.out.println(\"hello world\");")
                        .text("}")
                    .endCode()
                .endRef()
                .build();
        System.out.println(md);
    }

    @Test
    public void codeBlockWithOlUlTest() {
        String md = MdKiller.of()
                .title("Code Block with Ol and Ul (Embedded)")
                .code()
                    .ol()
                        .text("first")
                        .text("second")
                        .text("third")
                    .endOl()
                    .ul()
                        .text("first")
                        .text("second")
                        .text("third")
                    .endUl()
                    .text("")
                    .ref()
                        .ol()
                            .text("first")
                            .text("second")
                            .text("third")
                        .endOl()
                        .ul()
                            .text("first")
                            .text("second")
                            .text("third")
                        .endUl()
                    .endRef()
                .endCode()
                .build();
        System.out.println(md);


    }
}
