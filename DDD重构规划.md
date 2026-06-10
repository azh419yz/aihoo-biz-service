# Aihoo-Biz-Service DDD 重构规划

## 一、现状分析

根据代码探索，项目存在以下核心问题：

| 问题 | 现状 |
|------|------|
| **按层平铺** | 54 admin控制器、102 model全挤在 `system/controller`、`system/model` |
| **领域边界模糊** | Hospital/MDT/Prescription/Payment 混在一起 |
| **代码重复** | MdtOrder、DoctorUser 在多个模块有副本 |
| **命名不一致** | 有 `DicHospital` 也有 `YlGhdxxController`（含义不明）|
| **无聚合** | MdtTeam + MdtTeamDoctor + MdtTeamTag 相关但未分组 |

### 当前 Maven 模块

| 模块 | 用途 |
|------|------|
| `aihoo-admin` | Admin后端应用 (132 controller, 102 model, 102 mapper) |
| `aihoo-doctor-api` | 医生端API |
| `aihoo-patient-api` | 患者端API |

> **说明**：旧代码在 `aihoo/aihoo-biz-service/`，新代码在 `aihoo/aihoo-biz-new/`

### 当前包结构

```
aihoo-admin/
├── system/controller/    # 54个控制器混合在一起
├── system/service/        # 77+服务
├── system/model/          # 102个model平铺
├── system/mapper/         # 102个mapper
└── common/               # 上传、安全、excel、工具

aihoo-doctor-api/
├── app/controller/        # ~40个控制器
├── app/service/
├── app/model/
└── common/

aihoo-patient-api/
├── app/controller/        # ~25个控制器
├── app/service/
├── app/model/
└── common/
```

### 定时任务现状

| 模块 | 任务 |
|------|------|
| aihoo-admin | `OrderTimeoutTask` (1个) - 订单超时处理，应归属 `aihoo-domain-payment` |

> **说明**：`aihoo-job` 模块中的 57 个定时任务暂不处理，待后续独立处理。

## 二、现状核心问题

### 2.1 代码重复问题（最大风险）

三个 API 模块各自独立一套 model/mapper/service，实际共库但代码隔离：

| 实体 | admin | doctor-api | patient-api | 说明 |
|-----|-------|-----------|------------|------|
| MdtOrder | 1份 | 1份 | 1份 | 三份副本，内容相同 |
| MdtTeam | 0份 | 0份 | 1份 | 仅 patient-api 有 |
| DoctorUser | 1份 | 1份 | 1份 | 三份副本 |
| PatientUser | 0份 | 1份 | 1份 | 两份副本 |
| HosPrescription | 0份 | 1份 | 1份 | 两份副本 |
| ImMsg/ImMsgContent | 0份 | 1份 | 1份 | 两份副本 |

### 2.2 大方法问题（迁移高风险）

以下 ServiceImpl 超过 5000 行，必须先拆分再迁移：

| 文件 | 模块 | 行数 |
|-----|------|------|
| MdtOrderServiceImpl | patient-api | ~2500+ |
| PatientUserServiceImpl | patient-api | ~2500+ |
| PayServiceImpl | patient-api | ~2000+ |
| HosRevisitServiceImpl | patient-api | ~1800+ |
| MdtOrderServiceImpl | admin | ~2500+ |
| DoctorUserServiceImpl | admin | ~2000+ |
| OfflineOderServiceImpl | admin | ~2000+ |
| MdtServiceImpl | admin | ~1500+ |
| PrescriptionServiceImpl | doctor-api | ~1300+ |

### 2.3 跨域依赖问题（需要解耦）

**IM域对处方域的依赖**（doctor-api 和 patient-api 的 ImController）:

```java
// ImController.findImMsgByVisitNo() 中的问题代码
if ("savePrescription".equals(dataJson.getString("type"))) {
    String hosPrescriptionId = dataJson.getJSONObject("data").getString("hosPrescriptionId");
    HosPrescription prescription = hosPrescriptionService.getById(hosPrescriptionId);  // 跨域直接调用！
    dataJson.getJSONObject("data").put("prescriptionStatus", prescription.getStatus());
}
```

**问题**：patient-api 直接注入了 doctor-api 的 HosPrescriptionService，共享同一数据库。当前可以工作，但分库后会挂。

### 2.4 聚合识别结果

| 聚合根 | 子实体 | 所在域 |
|-------|--------|-------|
| HosPrescription | YlDzcfmx, HosPrescriptionDrug, Drug | prescription |
| MdtOrder | MdtOrderDoctor, MdtOrderFile, MdtOrderReport | consultation |
| MdtTeam | MdtTeamDoctor, MdtTeamTag | consultation |
| Order | PayRecord, OrderItem | payment |
| Hospital | Department, Drugstore | hospital |
| SysUser | (无子实体) | sys |

## 三、目标 DDD 结构

**核心变化**：
- 业务域只有 Service 层，无 Controller
- Controller 按调用方拆分到 API 层
- 所有代码在同一层级目录，不再有嵌套

```
aihoo/aihoo-biz-new/                  # 重构后代码根目录
├── aihoo-infra-shared-kernel/        # 共享内核（独立git）
│   ├── common/                       #   基础组件 (BaseController, JsonResult, PageResult)
│   ├── config/                       #   配置类
│   ├── redis/                        #   Redis封装
│   ├── oss/                          #   OSS封装
│   ├── exception/                    #   异常体系
│   ├── constant/                     #   公共常量枚举
│   ├── excel/                        #   Excel注解
│   └── util/                         #   公共工具类（合并自三端）
│
├── aihoo-domain-sys/                 # 系统管理域（独立git，Service only）
│   └── service/                      #   SysUserService, SysRoleService, SysMenuService
│
├── aihoo-domain-hospital/            # 医院域（独立git，Service only）
│   └── service/                      #   HospitalService, DepartmentService, DrugstoreService
│
├── aihoo-domain-prescription/        # 处方域（独立git，Service only）
│   └── service/                      #   HosPrescriptionService
│
├── aihoo-domain-consultation/        # MDT会诊域（独立git，Service only）
│   └── service/                      #   MdtOrderService, MdtTeamService
│
├── aihoo-domain-payment/             # 支付域（独立git，Service only）
│   └── service/                      #   OrderService
│
├── aihoo-domain-visit/               # 就诊域（独立git，Service only）
│   └── service/                      #   VisitService, RevisitService
│
├── aihoo-domain-patient/             # 患者域（独立git，Service only）
│   └── service/                      #   PatientUserService
│
├── aihoo-domain-doctor/              # 医生域（独立git，Service only）
│   └── service/                      #   DoctorUserService
│
├── aihoo-domain-im/                  # 消息域（独立git，Service only）
│   └── service/                      #   ImMessageService
│
├── aihoo-api-admin/                  # Admin应用层（独立git）
│   ├── controller/                    #   全部从旧admin迁移过来，按域分组
│   ├── config/security/               #   认证配置（TokenFilter, SecurityConfig等）
│   ├── config/cors/                   #   跨域配置
│   └── request/                      #   请求DTO
│
├── aihoo-api-doctor/                 # 医生端API应用层（独立git）
│   ├── controller/                   #   调用各domain服务
│   └── config/security/              #   认证配置（独立）
│
└── aihoo-api-patient/                # 患者端API应用层（独立git）
    ├── controller/                   #   调用各domain服务
    └── config/security/              #   认证配置（独立）
```

## 四、核心域与聚合边界

### 4.1 系统管理域 (aihoo-domain-sys)

```
包含: SysUser, SysRole, SysMenu, 字典表(DicDepartment, DicHospital, DicMedicines, DicArea, DicDisease等)
边界: 用户管理、角色权限、字典数据、省市区数据

Service: SysUserService, SysRoleService, SysMenuService, DictService等
```

**省市区说明**：省市区是基础字典数据，被 Hospital、Department、Drugstore 等业务域引用，但本身属于系统基础数据，放在 sys 域统一管理。

### 4.2 医院域 (aihoo-domain-hospital)

```
Hospital聚合:
  Hospital (聚合根) → Department (科室) → Drugstore (药房)

边界: 医院管理、科室管理、药房管理、药品管理

Service: HospitalService, DepartmentService, DrugstoreService, DrugService

说明: Drug 药品归入 hospital 域（药房管理），被 prescription 通过 HosPrescriptionDrug 中间表引用
```

**业务特点**：
- 医院-科室关联关系管理
- Excel 批量导入导出
- 与省市区关联（省市区数据来自 sys 域）

### 4.3 处方域 (aihoo-domain-prescription)

```
Prescription聚合:
  HosPrescription (聚合根) → YlDzcfmx (处方明细)

边界: 处方创建、处方审核、处方发药、处方查询

Service: HosPrescriptionService, YlDzcfmxService

说明: Drug 药品归入 hospital 域（药房管理），处方通过 HosPrescriptionDrug 中间表引用 Drug
```

### 4.4 MDT会诊域 (aihoo-domain-consultation)

```
MdtOrder聚合:
  MdtOrder (聚合根) → MdtOrderDoctor → MdtOrderFile → MdtOrderReport

MdtTeam聚合:
  MdtTeam (聚合根) → MdtTeamDoctor → MdtTeamTag

边界: 会诊发起、会诊接单、会诊执行、会诊完成、团队管理

Service: MdtOrderService, MdtTeamService
```

### 4.5 支付域 (aihoo-domain-payment)

```
Order聚合:
  Order (聚合根) → PayRecord (支付记录) → OrderItem (订单项)

边界: 订单创建、支付发起、支付回调、订单完成、退款

Service: OrderService, PayRecordService

定时任务:
  - OrderTimeoutTask（来自 aihoo-admin，aihoo-job 暂不处理）
```

### 4.6 就诊域 (aihoo-domain-visit)

```
Visit聚合:
  HosVisit (聚合根)

Revisit聚合:
  HosRevisit (聚合根)

边界: 挂号、就诊记录、复诊管理

Service: HosVisitService, HosRevisitService

定时任务: 无（aihoo-job 暂不处理）
```

### 4.7 患者域 (aihoo-domain-patient)

```
边界: 患者注册、患者信息、患者健康档案

Service: PatientUserService
```

### 4.8 医生域 (aihoo-domain-doctor)

```
边界: 医生注册、医生信息、医生资质

Service: DoctorUserService

定时任务: 无（aihoo-job 暂不处理）
```

### 4.9 消息域 (aihoo-domain-im)

```
边界: IM消息、聊天记录、消息推送

Service: ImMessageService
```

## 五、Git仓库规划

### 5.1 架构模式：组件化架构（Module-based Monorepo）

**核心原则**：
- 每个业务域是**独立Git仓库**，通过Maven依赖调用
- 业务域**只有Service**，无Controller
- Controller按调用方分散到 `api-admin`、`api-doctor`、`api-patient` 三个服务
- 后期拆分为微服务时，只需为每个业务域添加Controller层

```
独立Git仓库模式
┌─────────────────────────────────────────────────────────────────┐
│  aihoo-infra-shared-kernel  (共享内核)                           │
│  - common/BaseController, JsonResult, PageResult                 │
│  - config/, redis/, oss/, exception/, mybatis/                   │
└─────────────────────────────────────────────────────────────────┘
                              ↓ Maven依赖
┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│ aihoo-      │  │ aihoo-      │  │ aihoo-      │  │ aihoo-      │
│ domain-sys  │  │ domain-     │  │ domain-     │  │ domain-     │
│ (Git仓库)   │  │ hospital    │  │ prescription│  │ consultation│
│ Service only│  │ (Git仓库)   │  │ (Git仓库)   │  │ (Git仓库)   │
└─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘
                              ↓ Maven依赖
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│ aihoo-      │  │ aihoo-      │  │ aihoo-      │
│ domain-     │  │ domain-     │  │ domain-     │
│ payment     │  │ visit       │  │ patient     │
│ (Git仓库)   │  │ (Git仓库)   │  │ (Git仓库)   │
└─────────────┘  └─────────────┘  └─────────────┘
                              ↓ Maven依赖
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│ aihoo-      │  │ aihoo-      │  │ aihoo-      │
│ domain-     │  │ domain-     │  │ domain-     │
│ doctor      │  │ im          │  │             │
│ (Git仓库)   │  │ (Git仓库)   │  │             │
└─────────────┘  └─────────────┘  └─────────────┘
                              ↓ Maven依赖
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│ aihoo-      │  │ aihoo-      │  │ aihoo-      │
│ api-admin   │  │ api-doctor  │  │ api-patient │
│ (Git仓库)   │  │ (Git仓库)   │  │ (Git仓库)   │
│ Controller  │  │ Controller  │  │ Controller  │
└─────────────┘  └─────────────┘  └─────────────┘
```

### 5.2 Git仓库清单

| 仓库 | 说明 | 依赖 |
|------|------|------|
| `aihoo-infra-shared-kernel` | 共享内核（各域依赖） | 无 |
| `aihoo-domain-sys` | 系统管理域（Service only） | shared-kernel |
| `aihoo-domain-hospital` | 医院域（Service only） | shared-kernel, sys |
| `aihoo-domain-prescription` | 处方域（Service only） | shared-kernel, hospital |
| `aihoo-domain-consultation` | MDT会诊域（Service only） | shared-kernel, hospital, prescription |
| `aihoo-domain-payment` | 支付域（Service only） | shared-kernel |
| `aihoo-domain-visit` | 就诊域（Service only） | shared-kernel |
| `aihoo-domain-patient` | 患者域（Service only） | shared-kernel |
| `aihoo-domain-doctor` | 医生域（Service only） | shared-kernel, sys |
| `aihoo-domain-im` | 消息域（Service only） | shared-kernel |
| `aihoo-api-admin` | Admin应用层（Controller） | 所有domain |
| `aihoo-api-doctor` | 医生端API应用层（Controller） | 所有domain |
| `aihoo-api-patient` | 患者端API应用层（Controller） | 所有domain |

**共13个Git仓库**

### 5.3 业务域内部结构

每个业务域（如 `aihoo-domain-xxx`）的目录结构：

```
aihoo-domain-xxx/
├── pom.xml                          # Maven配置，依赖 shared-kernel 和相关domain
└── src/main/java/com/aihoo/domain/
    └── xxx/
        ├── model/
        │   ├── entity/              # 实体类（如 MdtOrder.java）
        │   └── vo/                  # 值对象（如 MdtOrderVo.java）
        ├── mapper/                  # MyBatis Mapper 接口
        ├── service/                 # 领域服务接口
        │   ├── XxxService.java      # 接口
        │   └── XxxServiceImpl.java  # 实现
        └── repository/              # 仓储接口（如需要）
```

**注意**：业务域不包含 `controller/`，Controller统一放在API层。

### 5.4 API层内部结构

`aihoo-api-admin` 的目录结构：

```
aihoo-api-admin/
├── pom.xml                          # Maven配置，依赖所有domain
└── src/main/java/com/aihoo/admin/
    ├── controller/
    │   ├── hospital/
    │   │   └── HospitalController.java   # 调用 domain-hospital 服务
    │   ├── sys/
    │   │   └── SysUserController.java   # 调用 domain-sys 服务
    │   └── consultation/
    │       └── MdtOrderController.java   # 调用 domain-consultation 服务
    └── config/                      # API层配置（如事务、AOP）
```

### 5.5 依赖规则

| 规则 | 说明 |
|------|------|
| API层 → Domain层 | 通过Maven依赖直接调用Service |
| Domain层 → Domain层 | 通过Maven依赖直接调用Service |
| Domain层 → 共享内核 | 所有Domain依赖 shared-kernel |
| **禁止循环依赖** | **只能上层依赖下层，禁止反向依赖** |
| **依赖方向** | **sys → hospital → prescription → consultation → payment, visit, patient, doctor, im** |

**依赖方向图**：

```
shared-kernel (底层，无依赖)
     ↑
     │
sys ─┼─→ hospital ─┼─→ prescription ─┼─→ consultation
     │              │                 │
     │              ↓                 ↓
     │          payment           visit, patient, doctor, im
     │
     └──────────────────────────────────────────→ (其他域)
```

**说明**：
- `aihoo-infra-shared-kernel` 是最底层，所有域都依赖它
- `aihoo-domain-sys` 是第二层，被多数域依赖
- `aihoo-domain-hospital` 被 `aihoo-domain-prescription` 和 `aihoo-domain-consultation` 依赖
- `aihoo-domain-prescription` 被 `aihoo-domain-consultation` 依赖
- 其他域（payment, visit, patient, doctor, im）位于同一层级，可以相互依赖但需避免循环

### 5.6 开发工作流

1. **克隆后首次构建**：
   ```bash
   git clone aihoo-infra-shared-kernel && cd aihoo-infra-shared-kernel && mvn install
   git clone aihoo-domain-sys && cd aihoo-domain-sys && mvn install
   # ... 依次构建所有domain
   git clone aihoo-api-admin && cd aihoo-api-admin && mvn install
   ```

2. **修改业务域代码后**：
   ```bash
   cd aihoo-domain-xxx && mvn install   # 安装到本地Maven仓库
   # 其他依赖此domain的服务自动使用新版本
   ```

3. **批量构建**：
   ```bash
   # 使用 Maven reactor 按依赖顺序构建
   mvn clean install -pl aihoo-domain-xxx -am   # -am: 同时构建依赖的模块
   ```

### 5.7 未来演进：微服务化

当需要将业务域拆分为独立微服务时，只需：

1. 为业务域添加 `controller/` 包
2. 引入 Nacos/Consul 作为注册中心
3. 为API层添加 Feign Client
4. 配置网关路由

```
演进示例
┌─────────────────────────────────────────────────────────────┐
│                         API Gateway                          │
│                   (Nacos + Spring Cloud Gateway)              │
└─────────────────────────────────────────────────────────────┘
         ↓                    ↓                    ↓
┌─────────────┐        ┌─────────────┐        ┌─────────────┐
│ aihoo-api- │        │ aihoo-api-  │        │ aihoo-api-  │
│ admin      │        │ doctor      │        │ patient     │
│ (Feign)    │        │ (Feign)     │        │ (Feign)     │
└─────────────┘        └─────────────┘        └─────────────┘
         ↓                    ↓                    ↓
┌─────────────┐        ┌─────────────┐        ┌─────────────┐
│ aihoo-     │        │ aihoo-      │        │ aihoo-      │
│ domain-sys │        │ domain-     │        │ domain-     │
│ (独立部署) │        │ hospital    │        │ prescription│
└─────────────┘        └─────────────┘        └─────────────┘
```

## 六、重构步骤建议

| 阶段 | 内容 | 风险 |
|------|------|------|
| **Phase 0** | 目录结构整理：将所有代码整理到同一层级，删除 refactor-work 嵌套 | 低 |
| **Phase 0.1** | 工具类核对：对比 admin/doctor/patient 三端工具类，合并到 shared-kernel | 中 |
| **Phase 0.2** | 大方法拆分：将所有超过 500 行的 ServiceImpl 拆分到 500 行以内 | 高 |
| **Phase 1** | 创建 `aihoo-infra-shared-kernel`，迁移 `aihoo-core` 的通用代码（BaseController、GlobalExceptionHandler、RedisService、OssComponent）| 低 |
| **Phase 2** | 创建 `aihoo-domain-sys`，迁移系统管理相关代码（含省市区） | 中 |
| **Phase 3** | 创建 `aihoo-domain-hospital`，迁移医院/科室/药房/药品相关代码 | 中 |
| **Phase 4** | 创建 `aihoo-domain-prescription`，迁移处方相关代码 | 中 |
| **Phase 5** | 创建 `aihoo-domain-consultation`，迁移 MDT 相关代码 | 中 |
| **Phase 6** | 创建 `aihoo-domain-payment`，迁移支付相关代码（含OrderTimeoutTask） | 中 |
| **Phase 7** | 创建 `aihoo-domain-visit`，迁移就诊相关代码（含相关定时任务） | 中 |
| **Phase 8** | 创建 `aihoo-domain-patient`、`aihoo-domain-doctor`（含DoctorCA任务）、`aihoo-domain-im` | 中 |
| **Phase 9** | 迁移 `aihoo-api-admin` Controller：整体迁移旧 admin 的 controller，再处理方法名差异 | 高 |
| **Phase 10** | 迁移 `aihoo-api-doctor`、`aihoo-api-patient` Controller | 高 |

**详细迁移方案见**：`DDD重构任务清单与进度.md`

## 七、关键决策点（已确认）

| 决策 | 选择 | 原因 |
|------|------|------|
| Hospital/Department | 独立为 `aihoo-domain-hospital` | 业务性质与系统管理不同，涉及省市区联动等复杂业务逻辑 |
| 省市区 | 归入 `aihoo-domain-sys` | 基础字典数据，被多域引用，统一管理更合理 |
| 药品管理 | 归入 `aihoo-domain-hospital` | 药品是医院/药房的资源，药房管理药品 |
| IM域 | 独立为 `aihoo-domain-im` | IM消息是独立业务域，被 doctor/patient 共同使用 |
| 定时任务 | 分散到各相关域 | 每个域维护自己的定时任务，更内聚 |
| API层拆分 | 按调用方拆分为 admin/doctor/patient 三个应用 | 符合当前架构，按调用方自然划分 |
| 数据库策略 | 共库 | 迁移风险最低，后期可优化成分库 |
| 仓库策略 | 每个域独立 git 仓库 | 解耦发布，各域可独立演进 |
| 域内结构 | 域只有Service无Controller | Controller按调用方拆分到API层，后期微服务化只需为域添加Controller |
| **跨域关联查询** | **使用DTO组合** | **避免跨域直接查询，API层负责组装多域数据为DTO返回** |
| **循环依赖** | **禁止反向依赖** | **只能上层依赖下层（sys → hospital → prescription → consultation），后续遇问题再处理** |
| **认证配置** | **分散到各API服务** | **各端认证方式不同（admin用token+shiro MD5，doctor/patient用独立认证），不放在shared-kernel** |

## 八、迁移注意点

### 8.1 迁移前置工作（必须先做）

**大方法拆分**：以下 ServiceImpl 必须先拆分到 500 行以内，否则迁移后依然无法维护：
- `MdtOrderServiceImpl` (patient/admin) - ~2500 行
- `PatientUserServiceImpl` (patient) - ~2500 行
- `PayServiceImpl` (patient) - ~2000 行
- `DoctorUserServiceImpl` (admin) - ~2000 行

### 8.2 跨域依赖解耦

**IM域调用处方域的问题**：

当前代码中 ImController 直接注入了 HosPrescriptionService，这是跨域直接调用：

```java
// doctor-api ImController.java 第107-115行
HosPrescription prescription = hosPrescriptionService.getById(hosPrescriptionId);
```

**解决方案**：迁移后通过 Feign Client 调用

```java
// IM域对外暴露的接口
@FeignClient("aihoo-domain-prescription")
public interface PrescriptionQueryClient {
    @GetMapping("/internal/prescription/{id}/status")
    PrescriptionStatusVO getStatus(@PathVariable String id);
}
```

### 8.3 聚合边界保持

迁移时保持聚合完整性，不破坏现有业务：

| 聚合根 | 子实体 | 必须一起迁移 |
|-------|--------|-------------|
| Hospital | Department, Drugstore | 是 |
| MdtOrder | MdtOrderDoctor, MdtOrderFile, MdtOrderReport | 是 |
| MdtTeam | MdtTeamDoctor, MdtTeamTag | 是 |
| HosPrescription | YlDzcfmx | 是 |

### 8.4 API层改造原则

改造后的 API 层（aihoo-api-*）只做：
- 请求路由
- 参数校验
- 调用 domain 服务
- 响应组装

**不做**：业务逻辑、事务边界、数据库操作

### 8.5 跨域关联查询：DTO组合模式

当需要展示来自多个域的数据时（如 MdtOrder 展示 Drugstore 名称），使用 DTO 组合模式：

```
问题场景：
- MdtOrder (consultation域) 需要展示 Drugstore (hospital域) 的名称
- 如果在 MdtOrder 中直接查询 Drugstore，会形成跨域耦合

解决方案：API层负责DTO组装
┌─────────────────────────────────────────────────────────────────┐
│                    OrderFacadeController                        │
│  1. 调用 mdtOrderService.queryMdtOrderPage() 获取 MdtOrder 列表  │
│  2. 调用 drugstoreService.getById() 获取 Drugstore              │
│  3. 组装为 MdtOrderVo（包含 drugstoreName）                       │
│  4. 返回 PageResult<MdtOrderVo>                                 │
└─────────────────────────────────────────────────────────────────┘
```

**DTO 示例**：`MdtOrderVo` 包含来自多个域的数据

```java
// API层 - 组装DTO
MdtOrderVo vo = new MdtOrderVo();
vo.setId(mdtOrder.getId());
vo.setName(mdtOrder.getName());
// 跨域数据通过 service 查询后组装
Drugstore drugstore = drugstoreService.getById(mdtOrder.getDrugstoreId());
vo.setDrugstoreName(drugstore != null ? drugstore.getName() : null);
```

**规则**：
- 跨域数据组合在 API 层处理
- 各 domain 只返回自己的 Entity/VO
- 不在 domain 中直接引用其他 domain 的 Service（避免循环依赖）

### 8.6 共库下的事务处理

当前所有模块共库，迁移后保持这一设计：
- 跨 domain 的事务在 API 层处理
- 单 domain 事务在 domain 层处理
- 使用 `@Transactional` 明确事务边界

### 8.7 统一 domain model

迁移时需要消除重复代码，建立统一的 domain model：
- MdtOrder 统一为一份
- DoctorUser 统一为一份
- PatientUser 统一为一份

### 8.8 定时任务迁移

> **说明**：`aihoo-job` 模块中的 57 个定时任务暂不处理，待后续独立处理。

仅处理 `aihoo-admin` 中的定时任务：

| 原任务 | 归属域 |
|-------|--------|
| OrderTimeoutTask | payment（订单超时处理） |

## 九、API层方案对比分析

### 方案1：按调用方拆分（当前推荐方案）

```
前端：admin.aihoo.com  →  aihoo-api-admin
前端：doctor.aihoo.com  →  aihoo-api-doctor
前端：patient.aihoo.com →  aihoo-api-patient
```

**优点**：
- 与前端域名结构完全匹配
- 各端独立部署、独立扩容
- 各端认证/鉴权策略可以不同
- 故障隔离，一端挂不影响其他端

**缺点**：
- 三个 API 模块有重复代码（路由、校验逻辑）
- 需要维护三套相似的配置

### 方案2：统一API层（按路由）

```
前端：*.aihoo.com  →  统一API网关  →  按路由分发到domain服务
```

**优点**：
- 代码复用，一套代码服务所有端
- 统一认证、限流、日志

**缺点**：
- 前端域名已拆开，需要把三个前端合并成一个 SPA
- 单点故障，一个问题影响所有端
- 扩容不灵活，不能按端扩容
- 部署耦合，需要一起部署

### 结论

**前端域名已拆开，不能按路由拆分**。方案1（按调用方）更适合当前架构。

## 十、部署架构

### 三jar架构（推荐）

拆分后保持现有的三个 jar 部署架构，每个 jar 通过 Maven 依赖引用 domain 代码：

```
部署结构
┌─────────────────────────────────────────────────────────────────────┐
│                         admin.aihoo.com                             │
│                    java -jar aihoo-api-admin.jar                    │
│  ┌─────────────────────────────────────────────────────────────────┐ │
│  │ aihoo-api-admin (Controller/路由/校验)                          │ │
│  │ + aihoo-domain-sys (Maven依赖)                                   │ │
│  │ + aihoo-domain-hospital (Maven依赖)                              │ │
│  │ + aihoo-domain-prescription (Maven依赖)                          │ │
│  │ + aihoo-domain-consultation (Maven依赖)                          │ │
│  │ + aihoo-domain-payment (Maven依赖)                               │ │
│  │ + aihoo-domain-visit (Maven依赖)                                │ │
│  │ + aihoo-domain-patient (Maven依赖)                              │ │
│  │ + aihoo-domain-doctor (Maven依赖)                                │ │
│  │ + aihoo-domain-im (Maven依赖)                                   │ │
│  │ + aihoo-infra-shared-kernel (Maven依赖)                          │ │
│  └─────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                        doctor.aihoo.com                             │
│                    java -jar aihoo-api-doctor.jar                   │
│  ┌─────────────────────────────────────────────────────────────────┐ │
│  │ aihoo-api-doctor (Controller/路由/校验)                         │ │
│  │ + 所有 domain (Maven依赖)                                        │ │
│  └─────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                       patient.aihoo.com                             │
│                    java -jar aihoo-api-patient.jar                   │
│  ┌─────────────────────────────────────────────────────────────────┐ │
│  │ aihoo-api-patient (Controller/路由/校验)                        │ │
│  │ + 所有 domain (Maven依赖)                                        │ │
│  └─────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

### 架构说明

| 特性 | 说明 |
|------|------|
| 部署方式 | 三个独立 jar，保持现有前端域名架构 |
| 代码来源 | 通过 Maven 依赖引入各 domain jar 包 |
| 服务通信 | 域间通过 Spring Bean 直接注入（同进程内） |
| 注册中心 | 不需要（Nacos/Consul 等） |
| 数据库 | 共库（MySQL 8.0），各域共享同一数据库 |
| 构建顺序 | 先构建 shared-kernel → 各domain → API层 |

### 构建流程

```bash
# 1. 构建共享内核
cd aihoo-biz-new/aihoo-infra-shared-kernel && mvn clean install

# 2. 构建所有业务域（按依赖顺序）
cd aihoo-biz-new/aihoo-domain-sys && mvn clean install
cd aihoo-biz-new/aihoo-domain-hospital && mvn clean install
cd aihoo-biz-new/aihoo-domain-prescription && mvn clean install
cd aihoo-biz-new/aihoo-domain-consultation && mvn clean install
cd aihoo-biz-new/aihoo-domain-payment && mvn clean install
cd aihoo-biz-new/aihoo-domain-visit && mvn clean install
cd aihoo-biz-new/aihoo-domain-patient && mvn clean install
cd aihoo-biz-new/aihoo-domain-doctor && mvn clean install
cd aihoo-biz-new/aihoo-domain-im && mvn clean install

# 3. 构建API层
cd aihoo-biz-new/aihoo-api-admin && mvn clean package
cd aihoo-biz-new/aihoo-api-doctor && mvn clean package
cd aihoo-biz-new/aihoo-api-patient && mvn clean package
```

### 与原架构对比

| 对比项 | 原架构 | 拆分后架构 |
|--------|--------|-----------|
| jar 数量 | 3个 | 3个（不变） |
| 代码结构 | 按层平铺 | 按域拆分，独立仓库 |
| 部署方式 | 不变 | 不变 |
| 服务调用 | 同进程直接调用 | 同进程直接调用（不变） |
| 代码复用 | 各模块重复 | domain 层统一，各 api 层调用 |
| 仓库结构 | 单仓库多模块 | 多仓库（每个domain独立） |

### 未来演进

当前架构为**代码拆分、部署不变**。未来如需独立部署，可将 domain 层拆出为独立服务，引入 Nacos + Feign：

```
演进方向
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│ aihoo-api-   │───→│   Nacos      │───→│ aihoo-domain│
│ admin        │    │  (注册中心)   │    │    -sys     │
└──────────────┘    └──────────────┘    └──────────────┘
```

---

## 十二、迁移任务清单

详细迁移任务清单见：`DDD重构任务清单与进度.md`

该文档包含：
- 总体进度（已完成/进行中/待迁移）
- 各域 Service 层迁移状态
- 各域 Controller 层迁移状态
- 工具类核对清单
- 认证配置迁移清单
- 大方法拆分清单
- 待讨论问题

---

## 十一、待进一步讨论

| 决策 | 状态 | 说明 |
|------|------|------|
| API层方案 | ✅ 已确认 | 按调用方拆分（域名已拆开，不能合并） |
| Drug归属 | ✅ 已确认 | 归入 hospital 域（药房管理） |
| 大方法拆分粒度 | ✅ 已确认 | 按业务子集拆分 |
| 域间服务通信 | ✅ 已确认 | 直接注入（单体部署），未来独立部署时用 Feign |
| 部署架构 | ✅ 已确认 | 三个 jar，保持现有部署模式 |
| 目录结构 | ✅ 已确认 | 所有代码同一层级，无嵌套 |
| 工具类迁移 | ⏳ 待讨论 | B类工具类（实现不同）如何处理 |
| Service冲突 | ⏳ 待讨论 | 同名方法不同实现时保留哪个 |

*文档生成时间: 2026/06/02*
*最后更新: 2026/06/10 - 调整目录结构、更新迁移步骤、引用任务清单*