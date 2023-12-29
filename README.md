# SensiCheck

[SensiCheck](https://github.com/Brath1024/SensiCheck) 是一款基于【AC自动机】算法实现的高性能敏感词工具。[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/houbb/sensi-check/blob/master/LICENSE.txt)

> [在线体验](https://www.brath.cn)

## 创作理由&目的

为 [荔知AI助手](https://www.brath.cn) 提供一个高性能的、开箱即用的敏感词检测工具。

## 特性

- [基于 AC自动机 算法，性能约为 70W+ QPS](https://github.com/Brath1024/SensiCheck#性能测试)


- [检测工具多种接入方式一键过滤](https://github.com/Brath1024/SensiCheck#核心方法)
- [支持Spring配置Bean来启动Holder配置，自定义敏感词列表文件地址](https://github.com/Brath1024/SensiCheck#自定义配置)


## 变更日志

[点此查看](https://github.com/Brath1024/SensiCheck/blob/master/CHANGE_LOG.md)

# 快速开始

## 准备

- JDK17+

- Maven 3.x+

## Maven 引入

```xml
<dependency>
    <groupId>io.github.brath1024</groupId>
    <artifactId>sensi-check</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Maven Central Repository

```
https://central.sonatype.com/artifact/io.github.brath1024/sensi-check
```



# 核心方法

`SensiCheckUtil` 作为敏感词的工具类，核心方法如下：

| 方法              | 参数                                                         | 返回值       | 说明                                                 |
| ----------------- | ------------------------------------------------------------ | ------------ | ---------------------------------------------------- |
| check             | String text                                                  | String       | 单字符串检测，默认替换值为"*"                        |
| check             | String text, String replaceValue                             | String       | 单字符串检测，自定义替换值                           |
| check             | String text, SensiCheckType type                             | String       | 单字符串检测，自定义过滤策略                         |
| check             | String text, String replaceValue, SensiCheckType type        | String       | 单字符串检测，自定义替换值和过滤策略                 |
| multiStringChecks | List<String> texts                                           | List<String> | 多字符串检测，默认替换值为"*"，默认过滤策略为REPLACE |
| multiStringChecks | List<String> texts, SensiCheckType type                      | List<String> | 多字符串检测，自定义过滤策略，默认替换值为"*"        |
| multiStringChecks | List<String> texts, String replaceValue                      | List<String> | 多字符串检测，自定义替换值，默认过滤策略为REPLACE    |
| multiStringChecks | List<String> texts, String replaceValue, SensiCheckType type | List<String> | 多字符串检测，自定义替换值和过滤策略                 |
| contains          | String value                                                 | boolean      | 字符串是否包含敏感词                                 |
| list              | ...                                                          | List<String> | 列出所有敏感词                                       |



# 如何使用？

#### 判断是否包含敏感词

```java
String text = "你妹的";

SensiCheckUtil.contains(text);
```

#### 检测并替换字符串中的敏感词

```java
String text = "你妹的";

String check = SensiCheckUtil.check(text);

System.out.println(check);

Output:
**的
```

#### 检测并替换字符串中的敏感词，自定义替换符号

```java
String text = "你妹的";

String check = SensiCheckUtil.check(text, "#");

System.out.println(check);

Output:
##的
```

#### 检测并替换字符串中的敏感词，使用异常策略

```java
String text = "你妹的";

String check = SensiCheckUtil.check(text, SensiCheckType.ERROR);

System.out.println(check);

Output:
SenException(message=文本内容检测到敏感词，已进行删除处理。为了维护社区网络环境，请不要出现带有敏感政治、暴力倾向、不健康色彩的内容! 可能涉及到的敏感词：[你妹]
, value=你妹的, code=0)
```

#### 支持检测包含特殊字符的敏感词

```java
String text = "你#妹的，。";

String check = SensiCheckUtil.check(text);

System.out.println(check);
```



# 自定义配置

###### 在Spring环境中使用时，可以使用Bean配置的方式来自定义SensiHolder，目前支持自定义敏感词文件地址，后续将支持更多灵活配置，敬请期待。

##### 1.将自己的敏感词列表文件添加到资源目录中, 注意：敏感词列表需要以base64写入

```shell
add to resources/mySenwords.txt
```

##### 2.在您项目中的，交付给Spring托管的任意类中添加一个Bean定义

```java
@Bean
public SensiHolder sensiHolder() {
    return new SensiHolder("/mySenwords.txt");
}
```



# 性能测试

## 环境

###### 不同环境会有差异，但是比例基本稳定

```shell
处理器	12th Gen Intel(R) Core(TM) i5-1240P   1.70 GHz
机带 RAM 24.0 GB (23.7 GB 可用)
系统类型 64 位操作系统, 基于 x64 的处理器
```

## 测试效果记录

测试数据：100+ 字符串，循环 100W 次

```java
@Test
public void test1() {
    String randomText = RandomUtil.randomString("1234567890bcdefghiJKLMNOPQRSTUVWXYZ你他妈的", 100);

    long start = System.currentTimeMillis();
    for (int i = 0; i < 100_0000; i++) {
        SensiCheckUtil.check(randomText);
    }
    long end = System.currentTimeMillis();

    System.err.println("------------------ COST: " + (end - start));
    //------------------ COST: 1317
}
```

| 序号 | 场景               | 耗时 |
|:----|:-----------------|:----|
| 1 | 敏感词，默认替换字符 | 1317ms，约 70W QPS |



# 已在Nexus发布

![image-20231228163256440](https://github.com/Brath1024/SensiCheck/blob/master/doc/images/image-20231228163256440.png?raw=true)

