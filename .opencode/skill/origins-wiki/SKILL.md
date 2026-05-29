---
name: origins-wiki
description: origins附属模组的Wiki文档编写指南，包含格式化规范、术语表和本地化要求。
---

# Origins Wiki

你来负责为origin附属模组编写Wiki文档页面。

## 必须的步骤
1. 查看项目README文件，了解项目的背景和目标。
2. 确认需要的是哪种类型的文档：Power Type、Entity Action、BiEntity Action、Entity Condition 或 BiEntity Condition。
3. 确认使用的语言版本（简体中文或英文），默认为两者都提供。
4. 先阅读根目录下的 `README.md` 或 `AI.md` 了解项目的整体结构和实现，确保内容准确。
5. 撰写内容前参考已有的wiki页面风格，保持一致性。

## 文档格式规范

### 页面结构
- `# 🌐语言：| [English](url) | > 简体中文 < |` 或 `# 🌐Language: | > English < | [简体中文](url) |` — 页面顶部的语言选择标题
- `### ℹ️标题` — 类型名称（条件类型的中文使用“检查”前缀，而非“检测”）
- 一行简介 — 简明描述主要功能
- `类型 ID: oap:xxx` — 类型的命名空间ID
- `>**⚠️注：**` — 注意事项块（如有），**必须放在字段解释块上方**
- `**字段解释：**`：
  - 字段名
  - 字段类型
  - 字段默认值(使用行内代码块)
  - 字段描述
- 各个类型之间使用 `---` 分隔

### 字段描述规范
- **mode + amount 组合**：mode的接受值直接在字段描述中写明；amount的描述为与“将要参与运算的数值”相似的格式。
- **字段可选值**：所有可选值直接在字段描述中写清（如`接受scale或multiply`），不使用独立的“xxx可选值”文本块。

### 标题规范
- 一级标题中不要有括号内容（如“随机条件（带保底）”应改为“随机条件”）
- 类型的中文本地化使用“检查”而非“检测”

## 不要做的事情
- 无论如何都不要将“状态效果（status effect）”缩写为“效果”。
- “能力持有者”不要缩写为“持有者”。
- `condition` 是起源默认行为，不需要额外说明。

## 简体中文本地化
- condition -> 条件
- power -> 能力
- action -> 动作
- origin -> 起源
- entity action -> 实体动作
- bientity action -> 双实体动作
- entity condition -> 实体条件
- bientity condition -> 双实体条件
- block condition -> 方块条件
- damage condition -> 伤害条件
- hud render -> HUD渲染
- attacker -> 攻击者
- power holder -> 能力持有者
- mount -> 坐骑
- identifier -> 标识符
- text component -> 文本组件
- item stack -> 物品堆栈
- vector -> 向量
- comparison -> 比较方式
- array -> 数组
- attribute -> 属性
- damage type -> 伤害类型
- status effect -> 状态效果
- amplifier -> 倍率/等级
- advancement -> 进度
- game day -> 游戏日
- light level -> 光照等级

## 快速检查清单
- “检测”是否都改为了“检查”？
- “状态效果”是否被缩写为“效果”？
- “能力持有者”是否被缩写为“持有者”？
- 所有“注”块是否在字段解释块上方？
- 字段可选值是否已合并到字段描述中？
- 标题是否有括号内容需要移除？
- mode + amount 组合是否符合规范？
