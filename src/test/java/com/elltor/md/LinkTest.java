package com.elltor.md;

import com.elltor.md.util.MdKiller;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * @author liuqichun03
 * Created on 2022/11/15
 */
public class LinkTest {
    @Test
    public void singleParamTest() {
        String md = MdKiller.of()
                .title("single param")
                .link(null)
                .link("")
                .link("  ")
                .link("https://elltor.com")
                .build();
        System.out.println(md);
    }

    @Test
    public void doubleParamTest() {
        String md = MdKiller.of()
                .title("double params")
                .link(null, null)
                .link("", "")
                .link("", null)
                .link(null, "")
                .link(null, "https://elltor.com")
                .link("link", null)
                .link("link", "https://elltor.com")
                .build();
        System.out.println(md);
    }

    @Test
    public void multiLinksTest() {
        LinkedHashMap<String, String> urlMappings = new LinkedHashMap<>();
        urlMappings.put("link1", "https://elltor.com");
        urlMappings.put("link2", "https://elltor.com");

        String md = MdKiller.of()
                .title("multi links")
                .links(null)
                .links(Collections.emptyMap())
                .links(urlMappings)
                .build();
        System.out.println(md);
    }

    @Test
    public void namingMultiLinksTest() {
        LinkedHashMap<String, String> urlMappings = new LinkedHashMap<>();
        urlMappings.put("link1", "https://elltor.com");
        urlMappings.put("link2", "https://elltor.com");

        String md = MdKiller.of()
                .title("naming multi links")
                .links(null, null)
                .links("", null)
                .links("   ", null)
                .links("", Collections.emptyMap())
                .links("links", urlMappings)
                .build();
        System.out.println(md);
    }

}
