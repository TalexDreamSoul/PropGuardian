# Talexs 系列 - TalexFrame

> 简单而不冗杂

实现您的一切想法，只要您会一点语法。

### Prepare / 准备

- Jre (1.8+) 为框架提供基本运行环境
- MySQL (5.8+) 为框架提供基本数据存储
- Redis (6.0+) 为框架提供并发存储以及容错回复支持
- ~~ErLang/OTP (24.2+) 为RabbitMQ提供支持~~ (已停用)
- ~~RabbitMQ (3.9.12+) 为框架提供消息中转(并发)支持~~ (已停用)
- <del>ElasticSearch () 为框架提供搜索支持</del> (已停用)

~~请在一切安装好后进入 **application.yml** 配置相关配置启动即可~~

**全新 7.0.0 架构已加入自动配置系统**

### Launch/启动

#### Windows

> Windows用户应该保证您的系统至少为 Windows7

双击 start for Windows 即可

#### Unix

> Unix用户应该保证您的系统至少为Centos7+ 或基于 Centos7+ 的系统

双击 start for unix 即可

### Tips/注意事项

初次启动需要较长时间，启动完成后可通过配套网页进行框架管理。

启动后请**输入命令**关闭系统，而不是 '×' 或 'killtask'

### Plugins/插件

您可通过在 plugins 文件夹添加插件来扩展功能.

基本的 plugin 支持以下语言:

- Java/Kotlin
- JavaScript

除此之外，框架提供了 Http/Websocket 来额外支持其他的语言

官方额外支持的其他语言:

- 易语言

### Develop/开发

相关的开发示例可在 example/plugins 中找到

### Doc/文档

相关的文档可在 docs 中找到

docs/frame 中是框架提供的一系列 api 可为任何基于 JreVm 的语言使用

其他语言请见 **docs/frame/xxx** 如 *docs/frame/JavaScript*

### Contribute/贡献

欢迎提交您的 Bug/建议/插件 等，生态需要您的完善。

### Thanks/鸣谢

#### TalexDreamSoul(QQ2418876761) 主要作者