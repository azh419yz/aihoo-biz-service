# DDD 重构任务清单与进度

*创建时间: 2026/06/10*

---

## 一、总体进度

| 域 | 进度 | 说明 |
|----|------|------|
| aihoo-infra-shared-kernel | ✅ 完成 | 已迁移：config/constant/excel/exception/oss/properties/redis/util（共37个文件） |
| aihoo-domain-sys | 🔄 进行中 | 部分Service完成 |
| aihoo-domain-hospital | 🔄 进行中 | 部分Service完成 |
| aihoo-domain-prescription | 🔄 进行中 | 部分Service完成 |
| aihoo-domain-consultation | 🔄 进行中 | 部分Service完成 |
| aihoo-domain-payment | ⏳ 待迁移 | |
| aihoo-domain-visit | ⏳ 待迁移 | |
| aihoo-domain-patient | ⏳ 待迁移 | |
| aihoo-domain-doctor | ⏳ 待迁移 | |
| aihoo-domain-im | ⏳ 待迁移 | |
| aihoo-api-admin | ⏳ 待迁移 | Controller待从旧admin整体迁移 |
| aihoo-api-doctor | ⏳ 待迁移 | |
| aihoo-api-patient | ⏳ 待迁移 | |

**已完成**: 0/13 (0%) - 正在重构目录结构
**进行中**: 5/13 (38%)
**待迁移**: 8/13 (62%)

---

## 二、目录结构

### 2.1 目标结构

```
aihoo/aihoo-biz-new/                  # 重构后代码根目录
├── DDD重构规划.md
├── DDD重构任务清单与进度.md
├── aihoo-infra-shared-kernel/        # 共享内核（独立git）
│   ├── pom.xml
│   └── src/main/java/com/aihoo/
│       ├── common/                   # 基础组件
│       │   ├── BaseController.java
│       │   ├── JsonResult.java
│       │   ├── PageResult.java
│       │   └── ResultCode.java
│       ├── config/                   # 配置类
│       ├── redis/                    # Redis封装
│       ├── oss/                      # OSS封装
│       ├── exception/                 # 异常体系
│       ├── util/                     # 公共工具类（合并自三端）
│       └── annotation/               # 公共注解
│
├── aihoo-domain-sys/                 # 系统管理域（独立git）
├── aihoo-domain-hospital/            # 医院域（独立git）
├── aihoo-domain-prescription/        # 处方域（独立git）
├── aihoo-domain-consultation/        # MDT会诊域（独立git）
├── aihoo-domain-payment/             # 支付域（独立git）
├── aihoo-domain-visit/               # 就诊域（独立git）
├── aihoo-domain-patient/             # 患者域（独立git）
├── aihoo-domain-doctor/              # 医生域（独立git）
├── aihoo-domain-im/                  # 消息域（独立git）
│
└── aihoo-api-admin/                  # Admin应用层（独立git）
    ├── controller/                    # 全部从旧admin迁移过来
    ├── config/                       # 认证配置、过滤器等
    ├── request/                      # 请求DTO
    └── vo/                           # 响应DTO
```

### 2.2 各域内部结构

```
aihoo-domain-xxx/
├── pom.xml                           # 依赖 shared-kernel 的 BOM
└── src/main/java/com/aihoo/domain/xxx/
    ├── model/
    │   ├── entity/                   # 实体类
    │   └── vo/                        # 值对象
    ├── mapper/                       # MyBatis Mapper
    ├── service/                      # 领域服务（从三个旧代码合并）
    │   ├── XxxService.java
    │   └── XxxServiceImpl.java
    └── job/                           # 定时任务（如有）
```

---

## 三、Maven 依赖管理（方案C）

### 3.1 依赖架构

```
aihoo-infra-shared-kernel/
├── pom.xml
│   ├── 定义 dependencyManagement (BOM)
│   └── 包含: spring-boot, mybatis-plus, redis, mysql 等版本
│
aihoo-domain-xxx/
├── pom.xml
│   └── 引用 shared-kernel 的 BOM，不指定版本号
│
aihoo-api-admin/
├── pom.xml
│   └── 依赖所有 domain
```

### 3.2 BOM 定义位置

| 组件 | 位置 | 说明 |
|------|------|------|
| shared-kernel | 发布时携带 BOM | 定义所有依赖版本 |
| 各 domain | 引用 BOM | 不直接指定版本号 |
| API层 | 依赖所有 domain | 调用各域服务 |

---

## 四、工具类迁移方案

### 4.1 工具类分类

| 类别 | 说明 | 处理方式 |
|------|------|----------|
| **A. 完全相同** | admin/doctor/patient 三份代码完全一致 | 合并到 shared-kernel/util/ |
| **B. 功能相同实现不同** | 方法名相同但实现有差异 | 标记出来，后续讨论 |
| **C. 某端独有** | 某端独有的工具类 | 合并到 shared-kernel/util/ |

### 4.2 迁移规则

- **所有工具类最终都放在 shared-kernel/util/**，不分来源
- 遇到 B 类问题（实现差异）时标记出来，后续讨论解决方案
- 不在 API 层或 Domain 层单独存放工具类

### 4.3 已发现工具类（需核对）

| 工具类 | admin | doctor | patient | 处理 |
|--------|-------|--------|---------|------|
| StringUtils | ✅ | ✅ | ✅ | A类-合并 |
| DateUtils | ✅ | ✅ | ✅ | A类-合并 |
| FileUtils | ✅ | ✅ | ✅ | B类-待核对 |
| ExcelUtils | ✅ | ❌ | ❌ | C类-合并 |
| JsonUtils | ✅ | ✅ | ❌ | B类-待核对 |
| Md5Utils | ✅ | ✅ | ✅ | A类-合并 |
| HttpUtils | ✅ | ✅ | ✅ | B类-待核对 |
| ImUtils | ❌ | ✅ | ✅ | C类-合并 |
| SmsUtils | ❌ | ✅ | ❌ | C类-合并 |

### 4.4 核对任务

| 任务 | 状态 | 说明 |
|------|------|------|
| 对比三端工具类 | ⏳ 待执行 | 逐一对比实现差异 |
| 标记B类问题 | ⏳ 待执行 | 实现不同的工具类标记 |
| 合并A/C类工具类 | ⏳ 待执行 | 合并到 shared-kernel |

---

## 五、Service 合并迁移流程

### 5.1 合并原则

从 admin、doctor、patient 三个旧代码中迁移 service、mapper，合并重复方法。

```
旧代码三个Service                    新代码Service
────────────────────────────────────────────────────────────
admin/service/HospitalServiceImpl   aihoo-domain-hospital/
doctor-api/service/HospitalServiceImpl  └── HospitalServiceImpl
patient-api/service/HospitalServiceImpl      (合并为一个)
```

### 5.2 合并步骤

| 步骤 | 内容 | 说明 |
|------|------|------|
| 1 | 提取接口 | 从三个旧代码中提取 Service 接口，对比方法签名 |
| 2 | 合并接口 | 方法签名相同则合并为一个；不同则保留多个重载 |
| 3 | 提取实现 | 从三个旧代码中提取方法实现 |
| 4 | 处理冲突 | 同名方法实现不同，标记并讨论 |
| 5 | 迁移到新目录 | 将合并后的代码迁移到 `aihoo-domain-xxx/service/` |

### 5.3 冲突处理规则

| 情况 | 处理方式 |
|------|----------|
| 方法签名相同，实现不同 | 标记为「冲突」，需讨论保留哪个实现 |
| 方法签名不同，功能相同 | 考虑统一接口，实现各自保留 |
| 某个旧代码独有方法 | 保留，可能需要标记「仅XX端使用」 |

---

## 六、Controller 迁移流程

### 6.1 迁移原则

- 直接将旧 admin 中的 controller 全部迁移到新 `aihoo-api-admin/controller/`
- 不合并（与 Service 不同）
- 如果方法名或参数变更，在 controller 中对应修改

### 6.2 迁移步骤

| 步骤 | 内容 | 说明 |
|------|------|------|
| 1 | 批量迁移 | 将旧 admin/controller/ 下所有 Controller 迁移到新目录 |
| 2 | 按域分组 | 将 Controller 按功能分配到子目录（hospital/, sys/ 等） |
| 3 | 解决依赖 | 调整 import 语句，指向新的 domain service |
| 4 | 处理方法名差异 | 如果新 service 接口与旧代码有差异，在 Controller 中调整 |
| 5 | 验证编译 | 确保所有 Controller 可以编译通过 |

### 6.3 迁移后目录结构

```
aihoo-api-admin/controller/
├── hospital/
│   ├── HospitalController.java
│   ├── DepartmentsController.java
│   ├── DrugstoreController.java
│   └── DrugController.java
├── sys/
│   ├── SysUserController.java
│   ├── SysRoleController.java
│   ├── AreaController.java
│   └── ...
├── consultation/
│   ├── MdtOrderController.java
│   ├── MdtTeamController.java
│   └── OrderFacadeController.java
├── payment/
├── prescription/
├── visit/
├── doctor/
├── patient/
├── im/
└── common/
```

---

## 七、认证配置迁移方案

> **说明**：各API服务认证方式不同，认证配置分散到各服务，不放在 shared-kernel

### 7.1 认证相关组件

```
旧代码 admin/                         → 新位置
─────────────────────────────────────────────────────────
common/security/
  ├── JwtToken.java                   → aihoo-api-admin/config/security/
  ├── JwtUtil.java                    → aihoo-api-admin/config/security/
  ├── AuthInterceptor.java            → aihoo-api-admin/config/security/
  ├── PermissionInterceptor.java       → aihoo-api-admin/config/security/
  └── SecurityConfig.java              → aihoo-api-admin/config/security/

common/constant/
  └── UserConstant.java               → shared-kernel/constant/

common/annotation/
  ├── LoginRequired.java               → 各api服务的config/annotation/
  └── Permission.java                  → 各api服务的config/annotation/
```

### 7.2 各API服务认证配置

| 服务 | 认证方式 | 说明 |
|------|---------|------|
| aihoo-api-admin | Token + Shiro MD5 | 使用 heou-token header，MD5密码加密 |
| aihoo-api-doctor | 独立认证 | 医生端独立认证机制 |
| aihoo-api-patient | 独立认证 | 患者端独立认证机制 |

### 7.3 配置类迁移

| 配置类 | 旧位置 | 新位置 |
|--------|--------|--------|
| WebMvcConfig | admin | 各api服务 |
| WebSecurityConfig | admin | 各api服务（独立配置）|
| CorsConfig | admin | 各api服务 |
| RedisConfig | admin | shared-kernel |
| MybatisPlusConfig | admin | shared-kernel |

---

## 八、当前进行中的工作

### 8.1 目录结构整理

| 任务 | 状态 | 说明 |
|------|------|------|
| 创建 aihoo-biz-new 目录 | ✅ 完成 | 重构代码统一放在此目录下 |
| 整理 aihoo/ 目录结构 | 🔄 进行中 | 将重构代码移入 aihoo-biz-new |
| 确认目录结构 | ⏳ 待确认 | 用户确认后执行 |

### 8.2 工具类核对

| 任务 | 状态 | 说明 |
|------|------|------|
| 提取三端工具类 | ⏳ 待执行 | 对比 admin/doctor/patient |
| 标记差异 | ⏳ 待执行 | 标记 B 类问题 |
| 合并到 shared-kernel | ⏳ 待执行 | 合并 A/C 类 |

---

## 九、待讨论问题

| 问题 | 状态 | 说明 |
|------|------|------|
| B类工具类实现差异 | ⏳ 待讨论 | FileUtils/JsonUtils 等实现不同如何处理 |
| Service冲突 | ⏳ 待讨论 | 同名方法不同实现时保留哪个 |
| 目录结构确认 | ⏳ 待确认 | 整理完成后需用户确认 |

---

## 十、符号说明

| 符号 | 含义 |
|------|------|
| ✅ 完成 | 迁移完成，逻辑与旧代码一致 |
| 🔄 进行中 | 部分完成，需要继续完善 |
| ⏳ 待迁移/待执行 | 还未开始 |

---

*最后更新: 2026/06/10*