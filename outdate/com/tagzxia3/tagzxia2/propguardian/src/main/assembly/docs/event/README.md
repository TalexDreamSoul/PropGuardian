# TalexFrame - 事件篇(Events)

## 概述

#### 为了让各个开发者的代码注入同一个地方而不受到影响，独立调用，我们采取了 事件模式 也可称作 **发布-订阅模式** 

> 在框架内部采用 **SpringBoot** 提供的 *@EventListener* 完成事件，对于插件则采用 框架提供的 **EventProvider (@TalexSubscribe)**

#### 为了在最大限度提供事件的同时不影响框架性能，我们采用了插件等级制度，即 若您的插件需要监听整体流程需要放入 **plugins/super** 中，意味着需要向 *框架父体* 调用信息，同时 你需要对每一个高级的监听器采用 **@TalexSubscribe + @SuperSubscribe** 双注解模式

