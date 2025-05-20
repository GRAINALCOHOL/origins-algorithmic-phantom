# 起源：算法幻影（Origins：Algorithmic Phantom）

**| [English](README.md) | >简体中文< |**

## 简介

这是一个为基于Origins的数据包或整合包创作者而生的模组。

## 内容

### 全新内容

#### 能力（Power types）

##### 常规类型（Regular types）

- 倒计时（Countdown）

##### 操作类型（Action related）

- 获得药水效果时的操作（Action on effect grained）

##### 修改类型（Modify types）

- 修改进食速度（Modify eating speed）
- 修改怪物行为（Modify mob behavior）

##### 阻止类型（Prevent types）

- 阻止累积消耗度（Prevent exhaustion）

#### 操作类型（Action types）

##### 双实体类型（Bi-entity types）

- （对目标）造成基于（发起者）属性值的伤害（Damage by attribute）

##### 实体类型（Entity types）

- 切换倒计时状态（Toggle countdown）
- 给予伤害吸收量（Give absorption）
- 移除伤害吸收量（Remove absorption）
- 修改药水效果持续时间（Modify effect duration）
- 修改实体药水效果等级（Modify effect amplifier）

#### 条件类型（Condition types）

##### 双实体类型（Bi-entity types）

- 是否为队友（Is team member）

##### 实体类型（Entity types）

- 游戏日（Game day）
- 攻击冷却（Attack cooldown）
- 倒计时进度（Countdown progress）
- 倒计时是否在活动（Countdown is active）

### 原版增强

#### 直接增强

- 范围效果（Area of effect）
  - 可以指定最大生效目标数，在指定的范围内随机选取。

#### 同名替代

类型ID相同但使用本模组的命名空间即：“oap”。

- 状态效果（Status effect）
  - 可以额外指定：effects数组、check_all字段（决定是否对已持有的effect实例进行完全匹配）。
  - 可以留空effect/effects字段（将对已持有的effect实例进行任意匹配）。
  - 兼容infinite时长效果，视为时长极长。

### 按键绑定（Key binding）

8个新的按键绑定（从“备用按键2”到“备用按键9”）。
以便于开发者制作和玩家使用主动能力较多的起源。

### [能力徽章（Badges）](src/main/resources/assets/oap/textures/gui/badge)

数个新的能力徽章。
包括：数字1-19、[文案（Copywriting）](src/main/resources/assets/oap/textures/gui/badge/copywriting.png)、[事件（Event）](src/main/resources/assets/oap/textures/gui/badge/event.png)。

## 其它

作者希望这个模组能够容纳更多实用的功能，愿集思广益将模组完善。

模组名称来源于：[庭渡久诧歌 在东方刚欲异闻的终符](https://thbwiki.cc/%E5%BA%AD%E6%B8%A1%E4%B9%85%E4%BE%98%E6%AD%8C)。

将来可能会支持更多版本，但没有支持forge/neoforge的打算。
