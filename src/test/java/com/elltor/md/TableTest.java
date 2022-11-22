package com.elltor.md;

import com.elltor.md.util.MdKiller;
import org.junit.Test;

/**
 * @author liuqichun03
 * Created on 2022/11/15
 */
public class TableTest {

    @Test
    public void normalTest() {
        String[][] data = {
                {"姓名",  "姓别",  "芳龄", "身高"},
                {"不知火舞",null,   "18", "173"},
                {"孙策",  "男",    "23", "181"},
                {"李白",  "男",    null, "179"},
        };

        String md = MdKiller.of()
                .title("表格")
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
     * normal test
     */
    @Test
    public void normalTest2() {
        String[] titles = new String[] {"1", "2", "3", "4"};
        String[][] data = {
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
        };

        String md = MdKiller.of()
                .title("test table 1")
                .table()
                    .data(titles, data)
                .endTable()
                .build();

        System.out.println(md);
    }

    /**
     * 表头和数据的列数不一致测试
     */
    @Test
    public void uniqueRowColTest() {
        String[] titles = new String[] {"1", "2", "3", "4"};
        String[][] data = {
                {"1", "2", "3"},
                {"1", "2", "3"},
                {"1", "2", "3"},
                {"1", "2", "3"},
        };

        String md = MdKiller.of()
                .title("test table 2")
                .table()
                .data(titles, data)
                .endTable()
                .build();
        System.out.println(md);

        //---------------------------------------------------------------------------------

        String[] titles2 = new String[] {"1", "2", "3"};
        String[][] data2 = {
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
        };

        String md2 = MdKiller.of()
                .title("test table 2")
                .table()
                .data(titles2, data2)
                .endTable()
                .build();
        System.out.println();
        System.out.println(md2);
    }

    /**
     * 脏数据测试1
     */
    @Test
    public void dirtyDataTest1() {
        String[] titles = new String[] {null, "2", "3", "4"};
        String[][] data = {
                {"1", null, "3"},
                {"1", "2", "3"},
                {"1", "2", "3"},
                {"1", null, "3"},
        };

        String md = MdKiller.of()
                .title("test table 2")
                .table()
                .data(titles, data)
                .endTable()
                .build();
        System.out.println(md);
    }

    /**
     * 脏数据测试2
     */
    @Test
    public void dirtyDataTest2() {
        String[] titles = new String[] {null, "2", "3", "4"};
        String[][] data = {
                {"1", null, "3"},
                {"1", "2", "3"},
                {"1", "2", "3"},
                {"1", null, "3"},
        };
        String md = MdKiller.of()
                .title("test table 3")
                .table()
                .data(titles, null)
                .endTable().build();
        System.out.println(md);

        String md2 = MdKiller.of()
                .title("test table 3")
                .table()
                .data(null, data)
                .endTable()
                .build();
        System.out.println();
        System.out.println(md2);
    }

    /**
     * 脏数据测试3
     */
    @Test
    public void dirtyDataTest3() {
        String md = MdKiller.of()
                .title("test table 3")
                .table()
                .data(null, null)
                .endTable()
                .build();
        System.out.println(md);
    }

    /**
     * 脏数据测试4
     */
    @Test
    public void dirtyDataTest4() {
        String md = MdKiller.of()
                .title("test table 4")
                .table()
                .endTable()
                .build();
        System.out.println(md);
    }

}
