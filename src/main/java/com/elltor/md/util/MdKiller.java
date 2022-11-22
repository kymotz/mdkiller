package com.elltor.md.util;

import java.util.*;

/**
 * bigTitle、title、subTitle、context text、context k-v text、
 * code block、link、table(matrix 2*3)、order list、un-order list
 *
 * @author liuqichun03 <liuqichun03@kuaishou.com>
 * Created on 2022/11/8
 */
public class MdKiller {

    // ~ APIs
    // -----------------------------------------------------------------------------------------------------------------
    public static SectionBuilder of() {
        return new SectionBuilder(new Section(Section.Type.NORMAL, null, null, null, 0));
    }

    // ~ public classes & public constants & public enums
    // -----------------------------------------------------------------------------------------------------------------
    public enum Style {
        NORMAL("normal"), BOLD("bold"), ITALIC("italic"),
        RED("red"), GREEN("green"), GRAY("gray"), YELLOW("gold"), BLUE("blue");

        private final String name;

        Style(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Fonts {
        public static final Fonts EMPTY = Fonts.of("");
        private final String text;
        // ~ private fields
        // -------------------------------------------------------------------------------------------------------------
        private Set<Style> styles = Collections.emptySet();

        private Fonts(String text, Style... style) {
            this.text = text != null ? text : "";
            if (style != null) {
                this.styles = new HashSet<>(Arrays.asList(style));
            }
        }

        // ~ public methods
        // -------------------------------------------------------------------------------------------------------------
        public static Fonts of(String text) {
            return new Fonts(text, Style.NORMAL);
        }

        public static Fonts of(String text, Style... style) {
            return new Fonts(text, style);
        }

        public boolean isEmpty() {
            return this.text == null || this.text.isEmpty();
        }

        @Override
        public String toString() {
            if (styles.contains(Style.NORMAL)) {
                return text;
            }
            String last = text;
            for (Style style : styles) {
                last = parseStyle(last, style);
            }
            return last;
        }

        // ~ private methods
        // -------------------------------------------------------------------------------------------------------------
        private String parseStyle(String text, Style style) {
            if (text == null || style == null) {
                return text;
            }
            switch (style) {
                case NORMAL:
                    break;
                case BOLD:
                    return "**" + text + "**";
                case ITALIC:
                    return "*" + text + "*";
                case RED:
                case GREEN:
                case BLUE:
                case YELLOW:
                    return "<font color='" + style.getName() + "'>" + text + "</font>";
            }
            return text;
        }
    }

    /**
     * 代表一行，可以是一个普通文本或一个K-V(s)数据
     */
    public static class MetaData {
        // ~ public constants
        // -------------------------------------------------------------------------------------------------------------
        public static final String DEFAULT_SEPARATOR = "：";
        public static final String DEFAULT_VALUE_SEPARATOR = " | ";
        public static final String LINK_TEMPLATE = "[%s▸](%s)";

        // ～ private fields
        // -------------------------------------------------------------------------------------------------------------
        private final Type type;
        private final Fonts text;
        private final Collection<Fonts> values;
        private final String separator = DEFAULT_SEPARATOR;
        private final String valueSeparator = DEFAULT_VALUE_SEPARATOR;

        public MetaData(Fonts text) {
            this(text, null);
        }

        public MetaData(Type type) {
            this(type, null, null);
        }

        public MetaData(Fonts text, Collection<Fonts> values) {
            this(Type.NORMAL, text, values);
        }

        public MetaData(Type type, Fonts text, Collection<Fonts> values) {
            this.type = type;
            this.text = text;
            this.values = values;
        }

        @Override
        public String toString() {
            return generateString(this.valueSeparator);
        }

        /**
         * generate one line
         */
        private String generateString(String valueSeparator) {
            boolean hasValues = values != null && !values.isEmpty();
            boolean hasText = text != null && !text.isEmpty();
            StringJoiner joiner = new StringJoiner(valueSeparator);
            String ret = "";
            switch (type) {
                case NORMAL:
                    if (hasText && hasValues) {
                        values.forEach(v -> joiner.add(v.toString()));
                        ret = text + separator + joiner;
                    } else if (!hasText && hasValues) {
                        values.forEach(v -> joiner.add(v.toString()));
                        ret = joiner.toString();
                    } else if (hasText) {
                        ret = text.toString();
                    }
                    break;
                case LINK:
                    if (hasText && hasValues) {
                        Fonts fonts = values.stream().findFirst().orElse(null);
                        if (fonts == null) {
                            break;
                        }
                        ret = String.format(LINK_TEMPLATE, text, fonts);
                    } else if (!hasText && hasValues) {
                        Fonts url = values.stream().findFirst().orElse(null);
                        if (url == null) {
                            break;
                        }
                        ret = String.format(LINK_TEMPLATE, url, url);
                    } else if (hasText) {
                        ret = String.format(LINK_TEMPLATE, text, text);
                    }
                    break;
                case LINK_LIST:
                    if (hasText && hasValues) {
                        ret = text + separator + generateLinkList(values);
                    } else if (!hasText && hasValues) {
                        ret = generateLinkList(values);
                    } else if (hasText) {
                        ret = String.format(LINK_TEMPLATE, text, text);
                    }
                    break;
                case BR:
                    ret = "<br>";
            }
            return ret;
        }

        // ~ private methods
        // -------------------------------------------------------------------------------------------------------------

        private String generateLinkList(Collection<Fonts> values) {
            if (values == null || values.isEmpty()) {
                return "";
            }
            Object[] valueArr = values.toArray();
            StringJoiner linkList = new StringJoiner(valueSeparator);
            for (int i = 0; i + 1 < valueArr.length; i += 2) {
                linkList.add(String.format(LINK_TEMPLATE, valueArr[i], valueArr[i + 1]));
            }
            boolean isPairNum = (valueArr.length % 2) == 0;
            if (!isPairNum) {
                String lastUrl = valueArr[valueArr.length - 1].toString();
                linkList.add(String.format(LINK_TEMPLATE, lastUrl, lastUrl));
            }
            return linkList.toString();
        }

        private enum Type {
            /** only plain text, plain text list with a name */
            NORMAL,
            /**
             * text : link name
             * values: index 0 is URL if existed.
             */
            LINK, LINK_LIST,
            BR,
        }
    }

    // ~ private class & private implements
    // -----------------------------------------------------------------------------------------------------------------
    private static class Section {
        private final int depth;
        private Type type;
        private Object data;
        private Section parent;
        private List<Section> children;

        private Section(Type type, Object data, Section parent, List<Section> children, int depth) {
            this.type = type;
            this.data = data;
            this.parent = parent;
            this.children = children;
            this.depth = depth;
        }

        // ~ public methods
        // -------------------------------------------------------------------------------------------------------------
        public void addChild(Section child) {
            lazyInitChildren();
            children.add(child);
        }

        public boolean childIsEmpty() {
            return children == null || children.isEmpty();
        }

        // ~ private methods
        // -------------------------------------------------------------------------------------------------------------
        private StringBuilder parse(StringBuilder latestData) {
            switch (type) {
                case LINK:
                case NORMAL:
                    latestData.append('\n').append(parseData(""));
                    return latestData;
                case BIG_TITLE:
                    latestData.append('\n').append(parseData("## "));
                    return latestData;
                case TITLE:
                    latestData.append('\n').append(parseData("### "));
                    return latestData;
                case SUBTITLE:
                    latestData.append('\n').append(parseData("#### "));
                    return latestData;
                case REF:
                    return parseRefSection(latestData);
                case CODE:
                    StringBuilder codeBlock = new StringBuilder(latestData.length() + 10);
                    codeBlock.append("\n```").append(latestData).append("\n```");
                    return codeBlock;
                case ORDER_LIST:
                    return parseOrderListSection(latestData);
                case UN_ORDER_LIST:
                    return parseUnOrderListSection(latestData);
                case TABLE:
                    return parseTableSection(latestData);
                case BR:
                    return latestData.append(parseData(""));
            }
            return latestData;
        }

        private String parseData(String prefix) {
            if (data == null) {
                return "";
            }
            return prefix + data;
        }

        private StringBuilder parseRefSection(StringBuilder latestData) {
            char[] chars = latestData.toString().toCharArray();
            StringBuilder data = new StringBuilder(chars.length * 2);
            if (chars[0] != '\n') {
                data.append("> ");
            }
            char last = 0;
            for (char c : chars) {
                if (last == '\n') {
                    data.append("> ");
                }
                data.append(c);
                last = c;
            }
            return data;
        }

        private StringBuilder parseOrderListSection(StringBuilder latestData) {
            char[] chars = latestData.toString().toCharArray();
            StringBuilder data = new StringBuilder(chars.length * 2);
            String padding = String.join("", Collections.nCopies(depth * 4, " "));
            int order = 1;
            if (chars[0] != '\n') {
                data.append(padding).append(order++).append(". ");
            }
            char last = 0;
            for (char c : chars) {
                if (last == '\n' && c != '\n' && c != ' ') {
                    data.append(padding).append(order++).append(". ");
                }
                data.append(c);
                last = c;
            }
            return data;
        }

        private StringBuilder parseUnOrderListSection(StringBuilder latestData) {
            char[] chars = latestData.toString().toCharArray();
            StringBuilder data = new StringBuilder(chars.length * 2);
            String padding = String.join("", Collections.nCopies(depth * 4, " "));
            if (chars[0] != '\n') {
                data.append(padding).append("- ");
            }
            char last = 0;
            for (char c : chars) {
                if (last == '\n' && c != '\n' && c != ' ') {
                    data.append(padding).append("- ");
                }
                data.append(c);
                last = c;
            }
            return data;
        }

        private StringBuilder parseTableSection(StringBuilder latestData) {
            if (data != null) {
                Object[][] tableData = (Object[][]) data;
                if (tableData.length > 0 && tableData[0].length > 0) {
                    StringJoiner titles = new StringJoiner(" | "), extras = new StringJoiner(" | ");
                    for (Object t : tableData[0]) {
                        titles.add(t != null ? t.toString() : "");
                        extras.add("-");
                    }
                    latestData.append("\n\n").append(titles).append('\n').append(extras);
                    for (int i = 1; i < tableData.length; i++) {
                        StringJoiner dataJoiner = new StringJoiner(" | ");
                        for (int j = 0; j < tableData[i].length; j++) {
                            dataJoiner.add(tableData[i][j] != null ? tableData[i][j].toString() : "");
                        }
                        latestData.append('\n').append(dataJoiner);
                    }
                }
            }
            return latestData.append('\n');
        }

        private void lazyInitChildren() {
            if (children == null) {
                children = new ArrayList<>();
            }
        }

        // ~ getter & setter
        // -------------------------------------------------------------------------------------------------------------
        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Section getParent() {
            return parent;
        }

        public void setParent(Section parent) {
            this.parent = parent;
        }

        public List<Section> getChildren() {
            return children;
        }

        public void setChildren(List<Section> children) {
            this.children = children;
        }

        public int getDepth() {
            return depth;
        }

        private enum Type {
            /**
             * data is {@link MetaData} and plain text
             */
            NORMAL,

            /**
             * data is {@link MetaData} and h2
             */
            BIG_TITLE,

            /**
             * data is {@link MetaData} and h3
             */
            TITLE,

            /**
             * data is {@link MetaData} and h4
             */
            SUBTITLE,

            /**
             * data is {@code null}, content is children
             */
            REF,

            /**
             * data is {@code null}, content is children
             */
            CODE,

            /**
             * data is matrix, aka String[][]
             */
            TABLE,

            /**
             * data is {@code null}, content is children
             */
            ORDER_LIST,

            /**
             * data is {@code null}, content is children
             */
            UN_ORDER_LIST,

            /**
             * data is {@link MetaData}
             */
            LINK,

            BR
        }
    }

    public static class SectionBuilder {
        private static final MdParser parser = new MdParser();
        /**
         * first is root
         */
        private final Section curSec;
        /**
         * code, ref curr -> par
         */
        private Section parentSec;
        /**
         * init null
         */
        private SectionBuilder parentBuilder;

        private SectionBuilder(Section curSec) {
            this.curSec = curSec;
        }

        private SectionBuilder(Section curSec, Section parentSec, SectionBuilder parentBuilder) {
            this.curSec = curSec;
            this.parentSec = parentSec;
            this.parentBuilder = parentBuilder;
        }

        // ~ public methods
        // -------------------------------------------------------------------------------------------------------------
        public SectionBuilder text(String text) {
            if (text != null) {
                curSec.addChild(new Section(Section.Type.NORMAL, new MetaData(Fonts.of(text, (Style) null)), curSec,
                        null, curSec.getDepth()));
            }
            return this;
        }

        public SectionBuilder text(String text, Style... style) {
            if (text != null) {
                curSec.addChild(new Section(Section.Type.NORMAL, new MetaData(Fonts.of(text, style)), curSec,
                        null, curSec.getDepth()));
            }
            return this;
        }

        public SectionBuilder text(Collection<String> values) {
            if (values != null && !values.isEmpty()) {
                text(null, values);
            }
            return this;
        }

        public SectionBuilder text(String name, Collection<String> values) {
            if (values == null || values.size() <= 0) {
                return text(name);
            }
            return text(name, null, values);
        }

        public SectionBuilder text(String name, Style valueStyle, Collection<String> values) {
            if (values == null || values.size() <= 0) {
                return text(name);
            }
            if (valueStyle == null) {
                valueStyle = Style.NORMAL;
            }
            List<Fonts> ele = new ArrayList<>(values.size());
            for (String value : values) {
                ele.add(Fonts.of(value, valueStyle));
            }
            curSec.addChild(new Section(Section.Type.NORMAL, new MetaData(Fonts.of(name), ele), curSec, null,
                    curSec.getDepth()));
            return this;
        }

        public SectionBuilder bigTitle(String title) {
            if (title != null && !title.isBlank()) {
                curSec.addChild(new Section(Section.Type.BIG_TITLE, new MetaData(Fonts.of(title)), curSec,
                        null, curSec.getDepth()));
            }
            return this;
        }

        public SectionBuilder title(String title) {
            return title(title, Style.NORMAL);
        }

        public SectionBuilder title(String title, Style color) {
            if (title != null && !title.isBlank()) {
                curSec.addChild(new Section(Section.Type.TITLE, new MetaData(Fonts.of(title, color)),
                        curSec, null, curSec.getDepth()));
            }
            return this;
        }

        public SectionBuilder title(String title, Fonts... label) {
            return title(title, null, label);
        }

        public SectionBuilder title(String title, Style titleColor, Fonts... label) {
            if (title != null && !title.isBlank()) {
                if (titleColor == null) {
                    titleColor = Style.NORMAL;
                }
                List<Fonts> labelList = label != null ? Arrays.asList(label) : Collections.emptyList();
                curSec.addChild(new Section(Section.Type.TITLE, new MetaData(Fonts.of(title, titleColor), labelList),
                        curSec, null, curSec.getDepth()));
            }
            return this;
        }

        public SectionBuilder subTitle(String title) {
            if (title != null && !title.isBlank()) {
                curSec.addChild(new Section(Section.Type.SUBTITLE, new MetaData(Fonts.of(title)),
                        curSec, null, curSec.getDepth()));
            }
            return this;
        }

        public SectionBuilder ref() {
            Section refSection = new Section(Section.Type.REF, null, curSec, new ArrayList<>(), curSec.getDepth());
            curSec.addChild(refSection);
            return new SectionBuilder(refSection, curSec, this);
        }

        public SectionBuilder endRef() {
            return this.parentBuilder != null ? this.parentBuilder : this;
        }

        public TableDataBuilder table() {
            return new TableDataBuilder(curSec, this);
        }

        public SectionBuilder link(String url) {
            return link(null, url);
        }

        public SectionBuilder link(String name, String url) {
            if (name == null || name.isBlank()) {
                name = url;
            }
            if (url != null && !url.isBlank()) {
                MetaData links = new MetaData(MetaData.Type.LINK, Fonts.of(name),
                        Collections.singletonList(Fonts.of(url)));
                curSec.addChild(new Section(Section.Type.NORMAL, links, curSec, null, curSec.getDepth()));
            }
            return this;
        }

        public SectionBuilder links(Map<String, String> urlMappings) {
            return links(null, urlMappings);
        }

        public SectionBuilder links(String name, Map<String, String> urlMappings) {
            if (urlMappings != null && !urlMappings.isEmpty()) {
                List<Fonts> serialUrlInfos = new ArrayList<>();
                for (Map.Entry<String, String> entry : urlMappings.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    serialUrlInfos.add(Fonts.of(key != null ? key : ""));
                    serialUrlInfos.add(Fonts.of(value != null ? value : ""));
                }
                Fonts wrappedName = name != null && !name.isBlank() ? Fonts.of(name) : Fonts.EMPTY;
                MetaData linksGroup = new MetaData(MetaData.Type.LINK_LIST, wrappedName, serialUrlInfos);
                curSec.addChild(new Section(Section.Type.NORMAL, linksGroup, curSec, null, curSec.getDepth()));
            }
            return this;
        }

        public SectionBuilder ol() {
            int depth = (curSec.getType() == Section.Type.ORDER_LIST || curSec.getType() == Section.Type.UN_ORDER_LIST)
                    ? curSec.getDepth() + 1
                    : curSec.getDepth();
            Section OrderListSec = new Section(Section.Type.ORDER_LIST, null, curSec, new ArrayList<>(), depth);
            curSec.addChild(OrderListSec);
            return new SectionBuilder(OrderListSec, curSec, this);
        }

        public SectionBuilder endOl() {
            return this.parentBuilder != null ? this.parentBuilder : this;
        }

        public SectionBuilder ul() {
            int depth = (curSec.getType() == Section.Type.ORDER_LIST || curSec.getType() == Section.Type.UN_ORDER_LIST)
                    ? curSec.getDepth() + 1
                    : curSec.getDepth();
            Section unOrderListSec = new Section(Section.Type.UN_ORDER_LIST, null, curSec, new ArrayList<>(), depth);
            curSec.addChild(unOrderListSec);
            return new SectionBuilder(unOrderListSec, curSec, this);
        }

        public SectionBuilder endUl() {
            return this.parentBuilder != null ? this.parentBuilder : this;
        }

        public SectionBuilder code() {
            Section codeSec = new Section(Section.Type.CODE, null, curSec, new ArrayList<>(), curSec.getDepth());
            curSec.addChild(codeSec);
            return new SectionBuilder(codeSec, curSec, this);
        }

        public SectionBuilder endCode() {
            return this.parentBuilder != null ? this.parentBuilder : this;
        }

        public SectionBuilder br() {
            curSec.addChild(new Section(Section.Type.BR, new MetaData(MetaData.Type.BR), parentSec, null,
                    curSec.getDepth()));
            return this;
        }

        public String build() {
            return parser.parse(curSec);
        }
    }

    public static class TableDataBuilder {
        private final Section parentSec;
        private final SectionBuilder parentBuilder;
        private Object[][] tableData;

        private TableDataBuilder(Section parentSec, SectionBuilder parentBuilder) {
            this.parentSec = parentSec;
            this.parentBuilder = parentBuilder;
        }

        // ~ public methods
        // -------------------------------------------------------------------------------------------------------------
        public TableDataBuilder data(Object[][] table) {
            if (table != null && table.length > 0 && table[0].length > 0) {
                tableData = table;
            }
            return this;
        }

        public TableDataBuilder data(Object[] title, Object[][] data) {
            if (title == null && data != null) {
                return data(data);
            }
            if (data != null && data.length > 0 && data[0].length > 0) {
                int minCol = Math.min(title.length, data[0].length);
                tableData = new Object[data.length + 1][minCol];
                tableData[0] = Arrays.copyOfRange(title, 0, minCol);
                for (int i = 0; i < data.length; i++) {
                    tableData[i + 1] = Arrays.copyOfRange(data[i], 0, minCol);
                }
            }
            return this;
        }

        public SectionBuilder endTable() {
            parentSec.addChild(new Section(Section.Type.TABLE, tableData, parentSec, null, parentSec.getDepth()));
            return parentBuilder;
        }
    }

    private static class MdParser {
        // ~ public methods
        // -------------------------------------------------------------------------------------------------------------
        public String parse(Section sec) {
            Section root = findRoot(sec);
            return doParse(root, root).toString().trim();
        }

        // ~ private methods
        // -------------------------------------------------------------------------------------------------------------
        private Section findRoot(Section sec) {
            if (sec.getParent() == null) {
                return sec;
            }
            return findRoot(sec.getParent());
        }

        private StringBuilder doParse(Section cur, Section root) {
            if (cur == null) {
                return null;
            }
            if (cur.childIsEmpty()) {
                return cur.parse(new StringBuilder());
            }
            StringBuilder childData = new StringBuilder();
            for (Section child : cur.getChildren()) {
                StringBuilder part = doParse(child, root);
                if (part != null) {
                    childData.append(part);
                }
            }
            return cur.parse(childData).append(cur.getParent() == root ? '\n' : "");
        }
    }
}