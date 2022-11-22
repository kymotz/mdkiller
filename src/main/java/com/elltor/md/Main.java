package com.elltor.md;

import com.elltor.md.util.MdKiller;

public class Main {
    public static void main(String[] args) {
        String md = MdKiller.of().text("Hello World").build();
        System.out.println(md);
    }
}
