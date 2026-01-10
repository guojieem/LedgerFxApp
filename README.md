# LedgerFX

### 专业级桌面账本 + JavaFX 架构模板

LedgerFX 是一个基于 Spring Boot 3 + JavaFX 的现代桌面个人账本系统，
采用模块化架构、StageManager 视图管理和 MyBatis-Plus 数据访问，
支持账单记录、分析与可扩展财务能力。


LedgerFX Controller 规范

所有 Controller

不允许直接 new Stage

不允许跨 Controller 调用

页面切换必须通过 StageManager
