# PropGuardian - 物业管理系统

## 项目简介

PropGuardian 是一个基于 Java Swing 开发的物业管理系统，旨在为物业公司提供全面的社区管理解决方案。系统采用 MVC 架构模式，集成了用户管理、社区信息维护、楼宇管理、房屋信息管理、收费管理、在线报修等核心功能。

## 主要功能

### 🏠 基础信息管理
- **社区信息维护**：管理小区基本信息，包括名称、地址、面积等
- **楼宇信息维护**：管理楼宇详细信息，如楼层数、总面积、楼高等
- **房屋信息维护**：维护房屋基本信息和状态
- **用户信息维护**：管理业主和用户基本信息

### 💰 收费管理
- **收费标准管理**：设置和修改各类收费标准
- **收费标准查询**：查询当前收费政策
- **物业账单报表**：生成和查看物业收费报表
- **用户账单报表**：为业主提供个人账单查询

### 🔧 服务管理
- **在线报修服务**：业主可在线提交报修申请，物业可及时处理
- **通知公告**：发布和管理社区通知公告
- **指数管理**：管理各类消费指数和抄表数据

### 👤 用户管理
- **登录认证**：安全的用户登录系统
- **权限管理**：基于角色的权限控制
- **用户注册**：新用户注册功能

## 技术架构

### 技术栈
- **开发语言**：Java 8+
- **UI框架**：Java Swing
- **数据库**：MySQL 8.0
- **ORM工具**：Hutool DB
- **工具库**：
  - Lombok：简化代码编写
  - Hutool：Java工具类库
  - MySQL Connector：数据库连接驱动

### 项目结构
```
src/
├── Main.java                    # 程序入口（测试用）
├── configuration/               # 配置管理
│   ├── Config.java             # 配置类
│   ├── Env.java                # 环境变量管理
│   └── resources/              # 配置资源文件
├── core/                       # 核心模块
│   ├── PropCore.java           # 系统核心类
│   ├── PropLauncher.java       # 系统启动器
│   └── StateData.java          # 状态数据管理
├── dao/                        # 数据访问层
│   ├── BaseEntity.java         # 基础实体类
│   ├── DaoInit.java            # 数据初始化
│   ├── entity/                 # 实体类
│   │   ├── UserInfo.java       # 用户信息实体
│   │   ├── CommunityInfo.java  # 社区信息实体
│   │   ├── Building.java       # 楼宇信息实体
│   │   ├── OwnerInfo.java      # 业主信息实体
│   │   └── ...                 # 其他实体类
│   └── upper/                  # 上层接口
├── db/                         # 数据库模块
│   ├── MySql.java              # MySQL数据库操作
│   └── schema.md               # 数据库结构文档
├── gui/                        # 用户界面
│   ├── Index.java              # 主界面
│   ├── LoginPanel.java         # 登录界面
│   ├── CommunityInfoPage.java  # 社区信息页面
│   ├── BuildingInfoPage.java   # 楼宇信息页面
│   ├── HouseInfoPage.java      # 房屋信息页面
│   ├── OwnerInfoPage.java      # 业主信息页面
│   ├── OnlineRepairService.java # 在线报修服务
│   ├── NotificationPanel.java  # 通知公告面板
│   ├── PropertyBillingReport.java # 物业账单报表
│   └── ...                     # 其他界面组件
└── utils/                      # 工具类
    ├── MentionUtil.java        # 消息提示工具
    └── ParserUtil.java         # 解析工具
```

## 快速开始

### 环境要求
- JDK 8 或更高版本
- MySQL 8.0 或更高版本
- IDE（推荐 IntelliJ IDEA）

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd PropGuardian
   ```

2. **配置数据库**
   - 创建 MySQL 数据库
   - 在 `src/configuration/resources/` 目录下创建 `.env` 文件
   - 配置数据库连接信息：
     ```properties
     mysql=jdbc:mysql://localhost:3306/propguardian?useSSL=false&serverTimezone=UTC
     user=your_username
     password=your_password
     ```

3. **导入依赖**
   - 项目使用的 JAR 包已包含在 `lib/` 目录中
   - 在 IDE 中添加这些 JAR 包到项目依赖

4. **运行项目**
   ```bash
   # 编译并运行
   java -cp "lib/*:src" core.PropLauncher
   ```

### 默认登录信息
- 用户名：`admin`
- 密码：`123456`
- 权限级别：`1`（管理员）

## 功能特性

### 🔐 安全特性
- 用户身份验证
- 基于角色的访问控制
- 数据库连接安全管理

### 📊 数据管理
- 完整的 CRUD 操作
- 数据一致性保证
- 自动数据初始化

### 🎨 用户界面
- 直观的 Swing GUI
- 响应式布局设计
- 用户友好的操作体验

### 🔧 系统特性
- 模块化架构设计
- 可扩展的插件系统
- 完善的日志记录

## 开发指南

### 添加新功能
1. 在 `dao/entity/` 中创建实体类
2. 在 `gui/` 中创建对应的界面组件
3. 在 `Index.java` 中添加菜单项
4. 实现相应的业务逻辑

### 数据库操作
- 继承 `BaseEntity` 类
- 实现 `storage()` 方法
- 使用 Hutool DB 进行数据库操作

### 界面开发
- 使用 Java Swing 组件
- 遵循现有的 UI 设计模式
- 使用 `MentionUtil` 进行用户提示

## 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 版本历史

- **v1.0.0** - 初始版本
  - 基础的物业管理功能
  - 用户登录和权限管理
  - 社区、楼宇、房屋信息管理
  - 在线报修服务
  - 收费管理系统

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 致谢

感谢所有为本项目做出贡献的开发者和用户！

---

**PropGuardian** - 让物业管理更简单、更高效！