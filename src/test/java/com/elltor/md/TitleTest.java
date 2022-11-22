package com.elltor.md;

import com.elltor.md.util.MdKiller;
import org.junit.Test;

/**
 * @author liuqichun03
 * Created on 2022/11/15
 */
public class TitleTest {

    @Test
    public void titleTest() {
        String md = MdKiller.of()
                .title(null)
                .title("")
                .title("  ")
                .title("title")
                .title("title", MdKiller.Style.RED)
                .title("title", MdKiller.Fonts.of("P1"), MdKiller.Fonts.of("P2", MdKiller.Style.GREEN),
                        MdKiller.Fonts.of("P3"))
                .title("title", MdKiller.Style.YELLOW, MdKiller.Fonts.of("RED", MdKiller.Style.RED),
                        MdKiller.Fonts.of("BLUE", MdKiller.Style.BLUE))
                .build();
        System.out.println(md);
    }

    @Test
    public void bigTitleTest() {
        String md = MdKiller.of()
                .bigTitle(null)
                .bigTitle("")
                .bigTitle("   ")
                .bigTitle("Big Title")
                .build();
        System.out.println(md);
    }

    @Test
    public void subTitleTest() {
        String md = MdKiller.of()
                .subTitle(null)
                .subTitle("")
                .subTitle("   ")
                .subTitle("Sub Title")
                .build();
        System.out.println(md);
    }

}
