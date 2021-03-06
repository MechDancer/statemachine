# 状态机的 kotlin 实现

[![Download](https://api.bintray.com/packages/mechdancer/maven/statemachine/images/download.svg) ](https://bintray.com/mechdancer/maven/statemachine/_latestVersion)
[![Build Status](https://www.travis-ci.org/MechDancer/statemachine.svg?branch=master)](https://www.travis-ci.org/MechDancer/statemachine)

## 说明

状态机发源于数学，作为图灵机的简化形式之一，在计算机中有极其重要的意义。尽管如此，理论化的状态机实在可以说是阳春白雪，难以在一般的场合应用。因此，认知广泛又实际上线的状态机模式，除了只闻其名的正则表达式之外，大概只有游戏的行为设计了。

关于机器人的行为设计，其实和游戏中角色的行为设计有非常强的一致性，因此把状态机应用到机器人中也就是顺理成章的了。此项目起源于FLL的单道批处理系统，又在将 FTC 的 loop 转化为时序的过程中发扬光大。期间经过各种小项目的检验，又经历了去年一段混乱低迷时期的实现的 Engine 版本，终于成为今天这样令人比较满意的样子。

在此也要感谢 .Net 下状态机库 [Stateless](http://www.hanselman.com/blog/Stateless30AStateMachineLibraryForNETCore.aspx) 带来的启发，希望这个简单的实现，能使有趣的状态机模式在机器人行为控制中得到发展。

*（2018 年 9 月 9 日）*

## 使用说明

定义一个状态机需要3步：

1. **定义状态**

   每个状态机具有一系列状态，这些状态不一定都要比状态机更早定义，但构造状态机时必须至少有一个已知的有效状态，即状态机初始状态。

	状态的定义包含4个可选项：

	1）动作（函数），状态机在当前状态所要执行的具体动作；

	2）前判断条件，满足状态的前判断条件才可进入状态，可用于约束初始状态，也可用于初始化资源；

	3）后判断条件，满足状态的后判断条件才可退出状态，也可用于释放资源；

	4）是否允许自动自循环，当状态动作执行完毕且没有合适的目标状态时，状态再次执行动作还是跳转到null。

- 同一个状态机必须具有相同类型的状态，且必须实现提供这些可选项的 `IState` 接口。

  我们推荐以下两种方法定义状态：

  **使用枚举**

  ```kotlin
  enum class StateTest : IState {
  	Init {
  		override val loop = false
  		override fun doing() = run { i = 0 }
  	},
  	Add {
  		override val loop = false
  		override fun doing() = run { ++i; Unit }
  		override fun before() = i < 20
  	},
  	Print {
  		override val loop = false
  		override fun doing() = run { println(i) }
  	}
  }
  ```

  **使用DSL**

  ```kotlin
  val init = state {
      doing = { i = 0 }
  }
  val add = state {
      before = { i < 20 }
      doing = { ++i }
  }
  val print = state {
      doing { println(i) }
  }
  ```

2. **构造状态机**

   ```kotlin
   val `for` = StateMachine(Init)
   ```

   提供初始状态即可。

3. **构造或注册事件**

   所谓事件，就是使状态机发生状态转移的函数。状态机提供了方法来构造事件。

   ```kotlin
   val event = `for`.event(Print to Init)
   ```

   通过状态机的引用可以构造事件，只需传递事件的源状态和目的状态即可。事件发生（调用）时，状态机会检查当前状态是否与状态的源匹配，以及两端节点是否接受状态转移。有时，用户可能并不急切地要求状态转移，而仅仅希望事件在状态的动作之间发生，这时可以将事件注册到状态机，状态机将在合适的时机自动调用。

   使用 `register` 方法：

   ```kotlin
   `for` register (Init to Print)
   ```

   基于定义，此模型也支持无限状态状态机，甚至在运行间动态添加新的状态和事件。

### 构造线性状态机

所谓线性状态机指的是状态有顺序并基于执行次数或外部条件按顺序调度的一类状态机。其本质与其他可执行后可自动跳转的状态机并无不同，因此库只需提供一个方便的构建器即可。

当前提供的 DSL 构建器：

```kotlin
val `for` = linearStateMachine {
		var i = 0
		once { i = 0 }
		call(20) { println(++i) }
		delay { time = 1; unit = SECONDS }
		once { println("hello world!") }
	}
```

`once` 指定的代码块将执行 1 次。可以通过 `call` 设置代码块的执行次数。还可以通过 `forever` 设置代码块无限循环（可以通过看门狗退出）。`delay` 用于在两个状态之间制造间隔。

状态指定的顺序就是状态执行的顺序。`reset` 可要求状态机回到初始状态，初始状态是无法修改的。

### 使用看门狗

有时状态执行较慢，或者状态的离开条件因为某些原因迟迟无法满足，我们希望状态机在最终无望转移时仍能继续执行下去，此时可以使用看门狗。构造看门狗时需要指定看门狗作用的状态机、其起始状态、目标状态和等待的时间。当调用看门狗的 `start` 方法后，看门狗将启动。当等待时间消耗殆尽，状态机会启动一次检查，如果当前状态是指定的起始状态，环境有满足目标状态的进入条件，就不再检查起始状态的离开条件，直接驱动一次跳转。

由于其强制跳转操作依赖于状态机的支持，看门狗作用的状态机必须实现 `IExternalTransferable<T>`。

示例代码：

```kotlin
val init = state {
			val dog = Watchdog(this@stateMachine, null, null, 5, SECONDS)
			before = { dog.start(); ACCEPT }
			doing = { i = 0 }
		}
```

此状态机将在 `init` 状态初次执行 5s 后直接结束。

## 示例

完整示例代码可以在 [这里](https://github.com/MechDancer/statemachine/blob/dev/src/test/kotlin/org/mechdancer/statemachine/test/StateTest.kt) 找到。

## 开始使用

* Gradle
* Maven
* Bintray

您需要将其添加至  [仓库和依赖](https://docs.gradle.org/current/userguide/declaring_dependencies.html) 中。

### Gradle

```groovy
repositories {
    jcenter()
}
dependencies {
    compile 'org.mechdancer:statemachine:0.1.1'
}
```

### Maven

```xml
<repositories>
   <repository>
     <id>jcenter</id>
     <name>JCenter</name>
     <url>https://jcenter.bintray.com/</url>
   </repository>
</repositories>

<dependency>
  <groupId>org.mechdancer</groupId>
  <artifactId>statemachine</artifactId>
  <version>0.1.1</version>
  <type>pom</type>
</dependency>
```

### Bintray

您总可以从 bintray 直接下载 jar：[![Download](https://api.bintray.com/packages/mechdancer/maven/statemachine/images/download.svg) ](https://bintray.com/mechdancer/maven/statemachine/_latestVersion)
