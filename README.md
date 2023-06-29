# MdKiller

MdKiller —— Markdown 杀手 🥷。

MdKiller 是一个格式化生成 Markdown 文本的工具，支持常用 Markdown 格式生成，例如引用块、代码块、有无序列表、表格等，内容上支持字体样式（style）和内容的嵌套，适用于 IM 消息 Markdown 排版。

本工具衍生版本：[@maomao124 - MarkdownUtils](https://github.com/maomao124/java_report_java_export_Markdown.git) 

## 一、使用教程

环境：JDK11

1、引入依赖，由于是单文件，直接把文件拷贝到项目即可使用。

2、使用，示例如下：

```java
@Test
public void test(){
    String md=MdKiller.of()
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
    .link("链接","https://elltor.com")
    .build();
    System.out.println(md);
}
```

输出 Markdown 文本：

```markdown
### 一个标题
文本
> 文本1
> 文本2
> - 文本3
> - 文本4

[链接▸](https://elltor.com)
```

**P.S. 更多演示可以参考单元测试。**

## 二、特性介绍

- 易用性：易于排版文案、支持主流 Markdown 语法内容，就像用代码写 Markdown。
- 通用性：生成内容通用，工具生成标准 Markdown 格式并保障最大兼容性。
- 扩展性：具备一定扩展性，扩展 API 简单；内容多样，理论上所有输入的文本均支持样式。
- 集成：除 JDK 外无依赖；小巧，单文件约 700+ 行代码；
- 鲁棒性：具备一定容错性，所有接口均经过单元测试，见目录 `test/java/com/elltor/md`。

## 三、最佳实践

在 IM 中排版消息是痛苦的，这个工具就是要解决这个问题，通常在 IM 排版消息会遇到下列问题：

1. 在程序中直接拼接很难对 Markdown 进行排版，拼接使人头大，维护更加麻烦。（P.S. 不反对使用模版，但当增加/删除模版中的参数时也是麻烦的）
2. 差的 Markdown 消息特点：文案没有结构、重点不突出、交互差，不能突出主题。

因此，我们改善 IM 消息实际上就是解决上面两个问题。

### 3.1 链式调用 vs 普通调用

用不同的风格生成 Markdown 文本。

```java
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
```

输出：

```markdown
### 标题
文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落
> 引用中的普通文本
> <font color='red'>引用中的普通文本-设置颜色</font>
> **引用中的普通文本-加粗**
> 名字：值1 | 值2 | 值3 | 值4 | 值5

[有问题点链接▸](https://elltor.com)
```

### 3.2 一个简洁消息模版

```java
/**
  * 通过标题、引用中的文本和超链接形成了一个较为有格式的排版
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
```

输出：

```markdown
### 标题
文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落
> 引用中的普通文本
> <font color='red'>引用中的普通文本-设置颜色</font>
> **引用中的普通文本-加粗**
>
> 名字：值1 | 值2 | 值3 | 值4 | 值5

[有问题点链接▸](https://elltor.com)
```

### 3.3 用列表使内容更清晰

```java
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
```

输出：

```markdown
### 标题
文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落 文本段落
- 引用中的普通文本
- <font color='red'>引用中的普通文本-设置颜色</font>
- **引用中的普通文本-加粗**
- 名字：值1 | 值2 | 值3 | 值4 | 值5

[有问题点链接▸](https://elltor.com)
```

### 3.4 使用表格

```java
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
```

输出：

```markdown
### 使用表格
<font color='red'>月老的记事本：</font>

姓名 | 姓别 | 芳龄 | 身高
- | - | - | -
不知火舞 |  | 18 | 173
孙策 | 男 | 23 | 181
李白 | 男 |  | 179

#### 嵌套表格
> <font color='gold'>月老的记事本：</font>
> 
> 姓名 | 姓别 | 芳龄 | 身高
> - | - | - | -
> 不知火舞 |  | 18 | 173
> 孙策 | 男 | 23 | 181
> 李白 | 男 |  | 179
```

### 3.5 生成模版以提高性能

```java
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
```

输出：

```markdown
### 消息模版标题
这是一个消息模版，你可以通过缓存消息模版以提高性能。
> 在引用中的消息 —— 这是一个消息模版，你可以通过缓存消息模版以提高性能。

[详情链接🔗▸](https://elltor.com)
```

### 3.6 灵活在文本中拼接 Markdown 样式

```java
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
```

输出：

```markdown
### 标题
> **文本**
> 名字：*值(斜体)*
> **加粗文本**
> [链接▸](https://elltor.com)

[详情链接🔗](https://elltor.com)
```

## 四、最后

欢迎使用，如果有 Bug 可以直接在 GitHub 提 Issue，使用建议、交流 `elltor(at)163.com`


