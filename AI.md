# Origins: Algorithmic Phantom (OAP) 技术文档

> **模组版本**: 0.5.2 | **Minecraft版本**: 1.20.1 | **加载器**: Fabric | **许可证**: GPL-3.0

## 项目概览

**Origins: Algorithmic Phantom** 是一个面向 Origins 数据包/整合包创作者的**库模组**。它为 Origins 系统提供新的 Action Type、Condition Type 和 Power Type，极大扩展了 Origins 的数据驱动能力。

### 基本信息

| 属性 | 值 |
|------|-----|
| 模组ID | `oap` |
| 作者 | GRAINALCOHOL (cn.grainalcohol) |
| Java版本 | 17 |
| Fabric Loader | >= 0.16.14 |
| Fabric API | >= 0.92.5+1.20.1 |
| Origins | v1.10.0 |
| Apoli | 2.9.0 |
| Calio | 1.11.0 |
| 可选依赖 | Immersive Messages API (用于 `oap:send_a_message`) |

### 目录结构

```
src/main/java/grainalcohol/oap/
├── OAPMod.java              # 主入口
├── action/                  # 动作类型
│   ├── bientity/            # 双实体动作 (1个)
│   ├── block/              # 方块动作 (1个, 测试用)
│   └── entity/             # 实体动作 (14个)
├── api/                     # 公共API接口 (2个)
├── condition/               # 条件类型
│   ├── bientity/           # 双实体条件 (3个)
│   └── entity/             # 实体条件 (11个)
├── config/                  # 配置系统 (2个)
├── init/                    # 注册初始化 (11个)
├── listener/                # 事件监听 (1个)
├── mixin/                   # Mixin注入 (9个 + enhanced子目录6个)
├── network/                 # 网络数据包 (5个)
├── power/                   # 能力类型 (14个)
└── util/                    # 工具类 (5个)
```

---

## 功能详细列表

### 一、Power 类型 (能力)

所有 Power 类型都支持 Apoli 通用的 `condition` 字段。

#### 1. `oap:countdown` — 倒计时系统

功能强大的计时器，支持结束操作、间隔操作和 HUD 渲染。渲染方向与一般进度条相反（从右至左）。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `countdown` | int | 200 | 可选 | 倒计时总时长 (tick) |
| `ending_action` | EntityAction | null | 可选 | 倒计时结束时触发的操作 |
| `immediately_start` | boolean | true | 可选 | 是否立即开始倒计时 |
| `per_time_action` | EntityAction | null | 可选 | 每次间隔触发的操作 |
| `action_interval` | int | 20 | 可选 | 间隔操作的间隔 (tick) |
| `hud_render` | HudRender | null | 可选 | HUD 渲染配置 |

**相关类型**:
- `ToggleCountdownAction` — 控制启停
- `CountdownProgressCondition` — 检查进度
- `CountdownIsActiveCondition` — 检查是否活跃

#### 2. `oap:action_on_effect_gained` — 获得效果时触发

实体获得状态效果时触发指定操作。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `entity_action` | EntityAction | — | **必需** | 触发的操作 |
| `effect` | Identifier | null | 可选 | 单个效果ID |
| `effects` | Identifier[] | null | 可选 | 多个效果ID |
| `include_update` | boolean | true | 可选 | 效果升级是否也触发 |
| `check_all` | boolean | false | 可选 | 是否需要同时持有所有效果 |

**说明**: `effect` 和 `effects` 都为空时，任何效果获得都会触发。

#### 3. `oap:modify_eating_speed` — 修改进食速度

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `mode` | String | "scale" | 可选 | 运算模式 |
| `amount` | float | 1.0 | 可选 | 速度倍率 |

**mode 可选值**:
- `"scale"` — 直接乘: `speed * max(0.1, amount)`
- `"multiply"` — 增量乘: `speed * max(0.1, 1 + amount)`

#### 4. `oap:modify_drinking_speed` — 修改饮用速度

与 `modify_eating_speed` 结构完全一致。

#### 5. `oap:modify_mob_behavior` — 修改怪物行为

修改怪物对能力持有者的行为。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `entity_condition` | EntityCondition | null | 可选 | 仅匹配的怪物才会被修改 |
| `behavior` | String | — | **必需** | 行为模式 |

**behavior 可选值**:
- `"FRIENDLY"` — 变为友好，不会攻击持有者
- `"PASSIVE"` — 变为被动，不会主动攻击持有者

#### 6. `oap:prevent_exhaustion` — 阻止饥饿消耗

无额外字段，等效于饱和效果。

#### 7. `oap:hide_status_bars` — 隐藏状态栏

无额外字段，隐藏玩家生命值/饥饿值显示。

#### 8. `oap:action_on_death` — 死亡时触发

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `entity_action` | EntityAction | null | 可选 | 死亡时触发的操作 |
| `attacker_condition` | EntityCondition | null | 可选 | 攻击者条件检查 |
| `damage_condition` | DamageCondition | null | 可选 | 伤害来源条件检查 |

**说明**: `damage_condition` 中 `amount` 类型检查始终收到 0。

#### 9. `oap:advancement_progress` — 进度追踪

将进度准则完成度通过起源 HUD 进度条显示。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `advancement` | Identifier | — | **必需** | 进度ID |
| `hud_render` | HudRender | null | 可选 | HUD 渲染配置 |
| `on_complete` | EntityAction | null | 可选 | 进度完成时触发 |

#### 10. `oap:prevent_movement_axis` — 轴向移动阻止

6个轴向独立控制，能力会同步给坐骑。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `prevent_positive_x` | boolean | false | 可选 | 阻止 +X 方向移动 |
| `prevent_negative_x` | boolean | false | 可选 | 阻止 -X 方向移动 |
| `prevent_positive_y` | boolean | false | 可选 | 阻止 +Y 方向移动 |
| `prevent_negative_y` | boolean | false | 可选 | 阻止 -Y 方向移动 |
| `prevent_positive_z` | boolean | false | 可选 | 阻止 +Z 方向移动 |
| `prevent_negative_z` | boolean | false | 可选 | 阻止 -Z 方向移动 |

#### 11. `oap:damage_reflection_percent` — 百分比伤害反射

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `mode` | String | "scale" | 可选 | 运算模式 |
| `amount` | float | 1.0 | 可选 | 反射倍率 |
| `check_source` | boolean | true | 可选 | 是否检查伤害来源 |
| `random_addition` | float | 0.0 | 可选 | 随机偏移量 |

**mode 可选值**:
- `"scale"` — 反射倍率: `max(0, amount)`
- `"multiply"` — 增量倍率: `1 + max(0, amount)`

#### 12. `oap:damage_reflection_flat` — 固定伤害反射

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `mode` | String | "add" | 可选 | 运算模式 |
| `amount` | float | 1.0 | 可选 | 反射值 |
| `check_source` | boolean | true | 可选 | 是否检查伤害来源 |
| `random_addition` | float | 0.0 | 可选 | 随机偏移量 |

#### 13. `oap:action_on_absorption_change` — 吸收量变化触发

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `increase_action` | EntityAction | null | 可选 | 吸收量增加时触发 |
| `decrease_action` | EntityAction | null | 可选 | 吸收量减少时触发 |

---

### 二、Entity Action 类型 (实体操作)

#### 1. `oap:modify_effect_duration` — 修改效果时长

兼容无限时长效果（无限效果只能被 `"set"` 修改）。计算后时长为负数时设为无限。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `effect` | Identifier | null | **必需** | 状态效果ID |
| `mode` | String | "add" | 可选 | 运算模式 |
| `amount` | float | 0.0 | 可选 | 时长变化值 (tick) |
| `is_ambient` | boolean | false | 必需 | 效果是否来源于信标 |
| `show_particles` | boolean | true | 必需 | 是否产生粒子 |
| `show_icon` | boolean | true | 必需 | 是否显示在GUI上 |

**mode 可选值**: `"add"`, `"set"`, `"scale"`, `"multiply"`

#### 2. `oap:modify_effect_amplifier` — 修改效果等级

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `effect` | Identifier | null | **必需** | 状态效果ID |
| `mode` | String | "add" | 可选 | 运算模式 |
| `amount` | int | 1 | 可选 | 等级变化值 |
| `is_ambient` | boolean | false | 必需 | 效果是否来源于信标 |
| `show_particles` | boolean | true | 必需 | 是否产生粒子 |
| `show_icon` | boolean | true | 必需 | 是否显示在GUI上 |

**mode 可选值**: `"add"`, `"set"`

#### 3. `oap:toggle_countdown` — 切换倒计时状态

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `power` | Identifier | null | 可选 | 单个能力ID |
| `powers` | Identifier[] | null | 可选 | 多个能力ID |
| `mode` | String | "start" | 可选 | 操作模式 |
| `allow_toggle_restart` | boolean | false | 可选 | toggle模式下已完成时是否允许重启 |

**mode 可选值**: `"start"`, `"restart"`, `"stop"`, `"toggle"`

#### 4. `oap:send_a_message` — 发送沉浸式消息

需要 Immersive Messages API。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `message_type` | String | null | 可选 | 消息类型: "normal"/"toast"/"popup" |
| `duration` | float | 10.0 | 可选 | 显示时长 (秒) |
| `text` | String | null | 可选 | 主文本内容 |
| `color` | int | 0xFFFFFF | 可选 | 主文本颜色 (十六进制RGB) |
| `size` | float | 1.0 | 可选 | 主文本大小 |
| `offset_x` | float | 0.0 | 可选 | 主文本X偏移 |
| `offset_y` | float | 0.0 | 可选 | 主文本Y偏移 |
| `align` | String | "CENTER_CENTER" | 可选 | 主文本对齐方式 |
| `anchor` | String | "CENTER_CENTER" | 可选 | 主文本锚点 |
| `fade_in` | float | 1.0 | 可选 | 主文本淡入时间 |
| `fade_out` | float | 1.0 | 可选 | 主文本淡出时间 |
| `subtitle` | String | null | 可选 | 副文本内容 |
| `subtitle_delay` | float | 0.0 | 可选 | 副文本延迟显示时间 |
| `subtitle_offset` | float | 10.0 | 可选 | 副文本偏移 |
| `subtitle_color` | int | 0xFFFFFF | 可选 | 副文本颜色 |
| `subtitle_fade_in` | float | 1.0 | 可选 | 副文本淡入时间 |
| `subtitle_fade_out` | float | 1.0 | 可选 | 副文本淡出时间 |
| `subtitle_align` | String | "CENTER_CENTER" | 可选 | 副文本对齐方式 |
| `subtitle_anchor` | String | "CENTER_CENTER" | 可选 | 副文本锚点 |

#### 5. `oap:summon_tamed` — 召唤已驯服生物

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `entity_type` | EntityType | null | **必需** | 可驯服实体类型ID |

**说明**: 如果目标实体类型无法驯服，生成会失败。

#### 6. `oap:apply_random_status_effect` — 随机施加效果

从注册表中随机选取指定数量的状态效果并应用。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `amount` | int | 1 | 可选 | 要应用的效果数量 |
| `max_duration` | int | 600 | 可选 | 最大持续时间 (tick) |
| `min_duration` | int | 20 | 可选 | 最小持续时间 (tick) |
| `max_amplifier` | int | 255 | 可选 | 最大效果倍率 |
| `min_amplifier` | int | 0 | 可选 | 最小效果倍率 |

#### 7. `oap:modify_origin` — 修改玩家起源

动态切换指定层上的起源，触发回调。仅对 ServerPlayerEntity 有效。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `layer` | Identifier | — | **必需** | 起源层ID |
| `origin` | Identifier | — | **必需** | 起源ID |

#### 8. `oap:say` — 发送消息

以实体身份发送聊天消息。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `text` | Text | "Hello World" | 可选 | 消息内容 |
| `anonymous` | boolean | false | 可选 | 是否匿名 |
| `broadcast` | boolean | true | 可选 | 是否广播给所有玩家 |

#### 9. `oap:toast` — 显示Toast通知

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `title` | Text | null | 可选 | Toast标题 |
| `description` | Text | null | 可选 | Toast描述 |
| `icon` | ItemStack | null | 可选 | Toast图标 |
| `toast_type` | String | "system" | 可选 | Toast类型 |
| `advancement_type` | String | "task" | 可选 | 成就类型 |
| `recipe_type` | String | "crafting" | 可选 | 配方类型 |

#### 10. `oap:modify_absorption` — 修改吸收量

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `mode` | String | "add" | 可选 | 运算模式 |
| `amount` | float | 1.0 | 可选 | 变化值 |

**mode 可选值**: `"add"`, `"set"`, `"scale"`, `"multiply"`

#### 11. `oap:knock_up` — 击飞

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `velocity` | float | 0.0 | 可选 | 施加的向上速度 (非负) |
| `affect_non_living` | boolean | false | 可选 | 是否影响非LivingEntity |

#### 12. `oap:debug` — 调试日志

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `log` | String | null | 可选 | 日志消息内容 |
| `id` | String | "Minecraft" | 可选 | Logger名称 |
| `entity_info` | boolean | true | 可选 | 是否输出实体信息 |
| `as_warning` | boolean | false | 可选 | 是否以warn级别输出 |

#### 13. `oap:to_other_player` — 对其他所有玩家执行操作

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `target_action` | EntityAction | null | 可选 | 对目标玩家执行的操作 |
| `target_condition` | EntityCondition | null | 可选 | 目标玩家条件 |
| `self_action` | EntityAction | null | 可选 | 对自己执行的操作 |
| `self_condition` | EntityCondition | null | 可选 | 自身条件 |
| `bientity_action` | BiEntityAction | null | 可选 | 双实体操作 |
| `bientity_condition` | BiEntityCondition | null | 可选 | 双实体条件 |
| `max_target` | int | 0 | 可选 | 最大目标数量 (0表示不限制) |

#### 14. `oap:to_block` — 对方块执行操作

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `position` | Vec3d | (0,0,0) | 可选 | 方块坐标 |
| `mode` | String | "fixed" | 可选 | 模式 |
| `block_condition` | BlockCondition | null | 可选 | 方块条件 (仅fixed模式) |
| `block_action` | BlockAction | null | 可选 | 要执行的方块操作 |

**mode 可选值**:
- `"fixed"` — 固定坐标
- `"offset"` — 相对实体坐标偏移

---

### 三、BiEntity Action 类型 (双实体操作)

#### `oap:damage_by_attribute` — 基于属性造成伤害

对目标实体造成基于操作实体指定属性一定倍率的伤害。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `attribute` | Identifier | null | **必需** | 参与伤害计算的属性ID |
| `mode` | String | "multiply" | 可选 | 属性值运算方式 |
| `amount` | float | 1.0 | 可选 | 参与计算的数值 |
| `damage_type` | Identifier | null | **必需** | 伤害类型ID |
| `allow_self_damage` | boolean | true | 必需 | 是否允许伤害自身 |

**mode 可选值**: `"add"`, `"scale"`, `"multiply"`

---

### 四、Entity Condition 类型 (实体条件)

#### 1. `oap:game_day` — 检测游戏天数

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `comparison` | Comparison | — | **必需** | 比较方式 |
| `compare_to` | int | 0 | 可选 | 比较值 |

#### 2. `oap:attack_cooldown` — 检测攻击冷却进度

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `comparison` | Comparison | — | **必需** | 比较方式 |
| `compare_to` | float | — | **必需** | 比较值 (0~1) |

#### 3. `oap:status_effect` — 增强效果检测

支持等级/时长范围、多效果匹配、兼容无限效果。

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `effect` | Identifier | null | 可选 | 单个效果ID |
| `effects` | Identifier[] | null | 可选 | 多个效果ID |
| `min_amplifier` | int | 0 | 可选 | 最小等级 |
| `max_amplifier` | int | 255 | 可选 | 最大等级 |
| `min_duration` | int | 20 | 可选 | 最小时长 |
| `max_duration` | int | 600 | 可选 | 最大时长 |
| `check_all` | boolean | false | 可选 | 是否需要同时持有所有效果 |
| `require_all` | boolean | false | 可选 | 是否要求所有效果都匹配 |

#### 4. `oap:countdown_progress` — 检测倒计时进度

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `power` | Identifier | null | 可选 | 单个能力ID |
| `powers` | Identifier[] | null | 可选 | 多个能力ID |
| `comparison` | Comparison | — | **必需** | 比较方式 |
| `compare_to` | float | — | **必需** | 比较值 (0~1) |
| `check_all` | boolean | false | 可选 | 是否需要所有倒计时都匹配 |

#### 5. `oap:countdown_is_active` — 检测倒计时是否活动

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `power` | Identifier | null | 可选 | 单个能力ID |
| `powers` | Identifier[] | null | 可选 | 多个能力ID |
| `check_all` | boolean | false | 可选 | 是否需要所有倒计时都匹配 |

#### 6. `oap:countdown_is_finished` — 检测倒计时是否完成

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `power` | Identifier | null | 可选 | 单个能力ID |
| `powers` | Identifier[] | null | 可选 | 多个能力ID |
| `check_all` | boolean | false | 可选 | 是否需要所有倒计时都匹配 |

#### 7. `oap:number_of_player` — 检测玩家数量

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `comparison` | Comparison | — | **必需** | 比较方式 |
| `compare_to` | int | 1 | 可选 | 比较值 |
| `dimension` | String | "any" | 可选 | 维度范围 |

**dimension 可选值**: `"any"`, `"same"`, `"other"`, 或指定维度ID

#### 8. `oap:light_level` — 检测光照等级

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `comparison` | Comparison | — | **必需** | 比较方式 |
| `compare_to` | int | — | **必需** | 比较值 (0-15) |
| `light_type` | String | "combined" | 可选 | 光照类型 |

**light_type 可选值**: `"combined"`, `"block"`, `"sky"`

#### 9. `oap:name` — 检测实体名称

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `name` | String | null | 可选 | 单个名称 |
| `names` | String[] | null | 可选 | 多个名称 |
| `mode` | String | "auto" | 可选 | 匹配模式 |
| `use_regex` | boolean | false | 可选 | 是否使用正则表达式 |

**mode 可选值**: `"auto"`, `"raw"`, `"custom"`, `"uuid"`

#### 10. `oap:is_friendly` — 检测是否友好生物

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `include_neutral` | boolean | false | 可选 | 是否包含中立生物 |
| `player_friendly` | boolean | true | 可选 | 玩家是否视为友好 |

**说明**: 玩家始终返回 false。

#### 11. `oap:random` — 随机条件 (带保底)

| 字段名 | 类型 | 默认值 | 必需 | 说明 |
|--------|------|--------|------|------|
| `chance` | float | 1.0 | 可选 | 触发概率 |
| `at_max` | boolean | false | 可选 | 保底是否已达上限 |
| `allow_pity` | boolean | false | 可选 | 是否启用保底 |
| `extra_pity_count` | int | 0 | 可选 | 额外保底计数 |
| `pool_id` | String | "default_pool" | 可选 | 卡池ID |

---

### 五、BiEntity Condition 类型 (双实体条件)

#### 1. `oap:is_team_member` — 检测队友关系

无额外字段，检测两个实体是否为队友。

#### 2. `oap:is_friendly` — 检测两实体是否友好

无额外字段，完整的 Minecraft 生物关系检查。

---

## 增强 Origins 功能

### 1. AOE 动作增强

新增目标区域形状和最大目标数限制。

| 新增字段 | 类型 | 默认值 | 说明 |
|---------|------|--------|------|
| `shape` | String | "CUBE" | 区域形状 |
| `height` | double | 4.0 | 区域高度 (PRISM/CYLINDER) |
| `max_target` | int | 0 | 最大目标数 (>0时随机打乱后取前N个) |

**shape 可选值**: `CUBE`, `SPHERE`, `PRISM`, `CYLINDER`

### 2. `play_sound` 增强

| 新增字段 | 类型 | 默认值 | 说明 |
|---------|------|--------|------|
| `client` | boolean | false | 是否仅在客户端播放 |

### 3. `add_velocity` 增强

| 新增字段 | 类型 | 默认值 | 说明 |
|---------|------|--------|------|
| `no_dampen` | boolean | false | 是否忽略地面减速检查 |

### 4. `ModifyDamageTakenPower` 修复

修复受伤无敌帧内仍执行 action 的 bug。可通过配置文件开关。

### 5. 能力授予/撤销音效

玩家获得/失去 Power 时在客户端播放音效，Origin 初始化阶段的批量授予不触发。

---

## Mixin 系统

### 主服务端 Mixin (9个)

| Mixin | 注入目标 | 功能 |
|-------|---------|------|
| `EntityMixin` | `Entity.move()` | 实现 PreventMovementAxisPower 的移动过滤 |
| `LivingEntityMixin` | `LivingEntity` | 保底数据存储、效果获得触发、伤害反射 |
| `MobEntityMixin` | `MobEntity.setTarget()` | 实现 ModifyMobBehaviorPower |
| `PlayerEntityMixin` | `PlayerEntity.addExhaustion()` | 实现 PreventExhaustionPower |
| `ItemMixin` | `Item.getMaxUseTime()` | 实现进食/饮用速度修改 |
| `BlockEntityMixin` | `BlockEntity` | 方块实体保底数据存储 |
| `PlayerAdvancementTrackerMixin` | `PlayerAdvancementTracker.grantCriterion()` | 实现 AdvancementProgressPower |
| `AdvancementProgressAccessor` | `AdvancementProgress` | 访问 requirements 字段 |

### Enhanced Mixin (6个)

| Mixin | 注入目标 | 功能 |
|-------|---------|------|
| `AreaOfEffectMixin` | `AreaOfEffectAction` | AOE增强: 新形状、max_target |
| `EntityActionsMixin` | `EntityActions` | play_sound/client, add_velocity/no_dampen |
| `ModifyDamageTakenPowerMixin` | `ModifyDamageTakenPower` | 无敌帧修复 |
| `PlayerOriginComponentMixin` | `PlayerOriginComponent` | Origin初始化标记 |
| `PowerEntityAccessor` | `Power` | 访问entity字段 |
| `PowerHolderComponentImplMixin` | `PowerHolderComponentImpl` | 能力音效系统 |

### 客户端 Mixin (1个)

| Mixin | 注入目标 | 功能 |
|-------|---------|------|
| `InGameHudMixin` | `InGameHud.renderStatusBars()` | 实现 HideStatusBarsPower |

---

## 网络数据包

| 数据包 | 包ID | 内容 | 用途 |
|--------|------|------|------|
| `SoundPacket` | `oap:sound_packet` | soundId, volume, pitch | 客户端音效播放 |
| `CountdownPacket` | `oap:countdown_packet` | powerTypeId, currentTimer, intervalTimer, isCountingDown | 倒计时状态同步 |
| `AdvancementProgressPacket` | `oap:advancement_progress_packet` | powerTypeId, progress | 进度完成率同步 |
| `ToastPacket` | `oap:toast_packet` | title, description, icon, toastType | Toast通知 |

---

## 配置系统

配置文件路径: `config/oap/common.json`

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `power.enableDamageTakenPowerFix` | boolean | true | 启用 ModifyDamageTakenPower 无敌帧修复 |
| `enhanced.grantPowerSoundEffect` | boolean | true | 启用授予 Power 音效 |
| `enhanced.grantPowerSoundEffectCooldown` | int | 20 | 授予音效冷却 (tick) |
| `enhanced.grantPowerVolume` | float | 1.0 | 授予音效音量 |
| `enhanced.revokePowerSoundEffect` | boolean | true | 启用撤销 Power 音效 |
| `enhanced.revokePowerSoundEffectCooldown` | int | 20 | 撤销音效冷却 (tick) |
| `enhanced.revokePowerVolume` | float | 1.0 | 撤销音效音量 |

---

## 客户端特性

### 按键绑定

注册 8 个额外按键绑定 (ternary 到 denary，编号 2-9)。

### HUD 修改

通过 `InGameHudMixin` 实现 `HideStatusBarsPower`，隐藏玩家生命值/饥饿值显示。

---

## API 接口

### `PityDataHolder`

保底数据接口，由 LivingEntity 和 BlockEntity 实现。

```java
int oap$getPityCount(String poolId)  // 获取保底计数
void oap$incrementPity(String poolId)  // 递增保底计数
void oap$resetPity(String poolId)  // 重置保底计数
```

### `PowerSoundControl`

能力音效控制接口，由 PowerHolderComponentImplMixin 实现。

```java
void oap$setFromOrigin(boolean fromOrigin)  // 设置是否为Origin初始化阶段
boolean oap$isFromOrigin()  // 查询是否为Origin初始化阶段
```

---

## 工具类

| 工具类 | 功能 |
|--------|------|
| `EntityUtil` | 实体工具: 获取Power、队友检测、友好关系判断、伤害源创建、名称匹配 |
| `MathUtil` | 数学工具: clamp、随机偏移、概率、非负数 |
| `MiscUtil` | 杂项工具: TextAnchor转换、保底系统核心逻辑、Power ID匹配 |
| `AreaShape` | AOE形状枚举: CUBE, SPHERE, PRISM, CYLINDER |
| `EntityRelation` | 函数式接口: 双向实体关系测试 |

---

## WIP (开发中)

- `ActionOnEffectImmunityPower` — 效果免疫触发
- `CategoryStatusEffectCondition` — 分类效果条件
- `DimensionCondition` (bientity) — 已实现但未注册
- `TestAction` (block) — 已实现但注册被注释

---

## 技术架构

### 架构设计

- 使用 **Fabric Loom split environment source sets**，客户端/服务端代码分离
- 使用 **Mixin** 注入原版和 Origins 代码，扩展其功能
- 通过 **Calio/Apoli** 注册系统注册自定义类型
- 网络通信使用 **Fabric Networking API**

### 关键技术点

1. **保底系统 (Pity)**: 通过 PityDataHolder 接口注入到 LivingEntity 和 BlockEntity，使用 NBT 持久化
2. **倒计时同步**: 服务器端管理时间逻辑，通过自定义数据包同步到客户端用于 HUD 渲染
3. **进度追踪**: 通过 Mixin 拦截 PlayerAdvancementTracker.grantCriterion()，实时计算 requirements 完成率
4. **生物关系系统**: EntityUtil.isFriendlyBetween() 实现完整的 Minecraft 生物关系判断
5. **MultiplePowerType 支持**: MiscUtil.matchesPowerId() 能够处理 Origins 的多重能力子能力ID匹配
6. **能力音效控制**: 通过 PowerHolderComponentImplMixin 结合 PlayerOriginComponentMixin 判断是否来自起源授予

### 代码风格

- 注释以中文为主，类级别 Javadoc 使用中文
- 使用 Java 17 特性: switch expressions, records, pattern matching
- 大量使用函数式接口 (BiConsumer, BiFunction, Predicate, Consumer)