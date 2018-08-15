# 状态机的 kotlin 实现

[![Build Status](https://www.travis-ci.org/MechDancer/statemachine.svg?branch=master)](https://www.travis-ci.org/MechDancer/statemachine)

此项目起源于 FTC 自动程序，用于将大循环式的机器人软件结构变换为顺序的形式，同时实现更多基于状态机模型的逻辑形式。项目的目的是为多种状态机模型提供 kotlin 下统一、友好的语法。

项目采用将从 FTC 的遗产开始，逐步优化并添加功能。

## 路线图

项目将提供多种状态机模型的内部实现和 dsl 配置方式，未来可能提供更复杂的解耦合模型和状态转移设定，包括：

1. 线性状态机
2. 递归状态机
3. 星形状态机
4. 通用状态机
5. 状态机嵌套

## 任务

- [x] 从FTC移植
- [x] 发布到仓库
- [ ] 完善文档
- [ ] 提供解耦合模型

