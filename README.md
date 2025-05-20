# Origins：Algorithmic Phantom

**| >English< | [简体中文](README-zh_cn.md) |**

## Introduction

This is a mod for creators of datapacks or modpacks based on Origins.

## Content

### New content

#### Power types

##### Regular types

- Countdown

##### Action related

- Action on effect grained

##### Modify types

- Modify eating speed
- Modify mob behavior

##### Prevent types

- Prevent exhaustion

#### Action types

##### Bi-entity types

- Damage by attribute (Inflict damage to the target based on the initiator attribute value)

##### Entity types

- Toggle countdown
- Give absorption
- remove absorption
- Modify effect duration
- Modify effect amplifier

#### Condition types

##### Bi-entity types

- Is team member

##### Entity types

- Game day
- Attack cooldown
- Countdown progress
- Countdown is active

### Original enhancement

#### Direct enhancement

- Area of effect
  - You can specify the maximum number of effective targets and randomly select within the specified range.

#### Replacement of the same name

The type ID is the same, but the namespace of this module is: "oap".

- Status effect
  - You can additionally specify: effects array, check_all field (decide whether to fully match the already held effect instance).
  - You can leave blank effect/effects fields (there will be arbitrary matching of the already held effect instance).
  - Compatible with the duration effect, considered as extremely long.

### Key binding

8 new key bindings (from "Active Power (Ternary)" to "Active Power (Denary)").
It is an origin that facilitates developers to make and players to use more proactive capabilities.

### [Badges](src/main/resources/assets/oap/textures/gui/badge)

Several new ability badges.
Including: Numbers 1-19, [Copywriting](src/main/resources/assets/oap/textures/gui/badge/copywriting.png), [Event](src/main/resources/assets/oap/textures/gui/badge/event.png).

## Others

The author hopes that this module can accommodate more practical functions and is willing to brainstorm to improve the module.

The module name comes from: [庭渡久诧歌 在东方刚欲异闻的终符](https://thbwiki.cc/%E5%BA%AD%E6%B8%A1%E4%B9%85%E4%BE%98%E6%AD%8C).

More versions may be supported in the future, but there is no intention to support forge/neoforge.

The above content is translated by machine.
