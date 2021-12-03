## 零、置顶——注意事项



### 一）、打印

​	如果你想在终端输出信息，请使用`PrintUtil`工具类。

​	

### 二）、配置文件

​	拉代码之前，请备份好自己的配置文件，以防止覆盖。特别是of命令的配置



### 三）、如果更新为2.1.3版本后报错

​	请将`config目录`中的`config.json`文件改为`sys-config.json`



### 四）、求个Star

​	利用摸鱼时间开发，求个Star



### 五）、更新步骤

- pull代码到本地
- 运行start.bat
- 执行`reload`命令



### 六）、有问题联系QQ

​	如果你有好玩的、有趣的、牛逼的或者你实现不了的命令，欢迎提交你的代码或想法，不要怜惜我！

​	2417960618。 备注：命令系统

​	



## 一、项目作用

​	当你写代码有一定时间了，你会发现，你的手不太愿意离开键盘去碰鼠标，即便你是使用笔记本的触摸板你也不愿意去用鼠标去操作，此时你终于明白`linux`是多么舒服，用命令几乎可以完成所有事，但无奈windows的cmd不会操作，又不想学，最后接受现实，还是要用鼠标去点击各种图标

​	如果你有上面的苦恼，Java程序员们有福了，只要你下载本项目，就可以实现你的梦想。而且支持自定义命令，各种骚操作等你实现





## 二、特性



- Java天下第一
- 高扩展。可以自定义命令、执行器、解析器等
- 支持windows命令
- 上手0成本





## 三、安装/启动

#### 一）、环境

- windows
- Java 8



#### 二）、安装/启动

​	下载或clone项目到本地，然后进入`start`目录，运行`start.bat`脚本即可



## 四、目录结构介绍

### 一）、项目结构

```
config  				// 配置文件目录
document 				// 命令文档目录，存放命令的介绍
data					// 数据目录，用于存放一些数据，如：缓存
start 					// 启动文件目录
	start.bat			// 这是打包好后的可执行文件，你可以给它创建一个快捷方式在桌面，方便使用
src 					// 源码目录
```



### 二）、源码结构

```
constant 	// 常量文件
dto			// 存放数据传输对象，用于加载命令，可扩展
execption	// 存放异常
executor	// 存放命令执行器，用于执行命令，可扩展
gui			// 窗体相关文件
job			// 存放“工作”对象，工作既一个命令要执行的事情
parse		// 存放命令解析器，用于解析命令，可扩展
util		// 存放工具类
```



## 五、系统运行流程

```
初始化：
	读取配置——》加载命令传输对象（CommandDto）——》读取命令到内存
执行命令时：
	输入命令到终端（Terminal）——》键盘监听（KeyListenerHandler）——》
	存入命令上下文（CommandContent）——》回车——》命令解析器（CommandParseHandler）——》
	命令执行器（CommandExecutor）——》执行工作（CommandJob.run）
```





## 六、核心类介绍	

​	如果你想现在就知道怎么自定义命令，可以直接跳过核心类的介绍。当然我还是建议你看一看。

​	[自定义命令](#七自定义命令)

### 一）、CommandJob

​	`CommandJob`是个接口。

```java
 	/**
     * 获得命令的配置
     * @return 返回命令的配置
     */
    public CommandConfig getCommandConfig();

   /**
     * 初始化
     * 在命令执行前调用
     */
    public default void init() { }


    /**
     * 初始化
     * 在窗口打开后
     */
    public default void windowOpened() { }

    /**
     * 初始化
     * 在窗口关闭时
     */
    public default void windowClosing() { }


    /**
     * 开始执行任务
     * @param command 命令对象
     */
    public void run(Command command);


 	/**
     * 获得命令执行器
     * @return  返回命令执行器
     */
    public default CommandExecutor getExecutor() {
        return new DefaultCommandExecutor();
    }

    /**
     * 获得命令解析器
     * @return 返回命令解析器
     */
    public default ParseHandler getParseHandler() {
        return new DefaultParseHandler();
    }
```

​	其中`getCommandConfig`和`run`方法**必须实现,**如有需要可自行扩展`getParseHandler`和`getExecutor`。分别对应**命令解释器**和**命令执行器**。

​	`getCommandConfig`方法需要返回一个命令的配置对象，详情请见[**CommandConfig**](#二）CommandConfig)对象

​	`run`方法就是要执行命令后要运行的代码，该方法会传入一个解析好的[**Command**](#三）Command)对象

​	`init`方法会在执行命令之前调用，它是一个`defalut`方法，如果你有需要在执行命令前做一些事情就可以实现一下它。

​	`windowOpened`方法会在窗口打开时执行的一个回调。

​	`windowClosing`方法会在窗口关闭时执行的一个回调。



### 二）、CommandConfig

​	命令配置对象

```java
   /**
     * 是否异步执行
     */
    private Boolean isAsynch = false;

    /**
     * 命令名字
     */
    private String commandKey;

    /**
     * 命令所属组
     */
    private String group = "default";

    /**
     * 命令的参数的配置
     * key：参数名
     * value：参数是否需要一个值，改值不会为null，除非没有输入该参数
     */
    private Map<String, Boolean> paramConfig = CollectionUtil.newHashMap();


    /**
     * 命令介绍
     */
    private String introduce = "没有任何介绍。。。";

    /**
     * 命令描述
     * 字数限制15个字
     */
    private String description = "没有任何描述";
```

​	

​	该对象用于配置命令，其中`commandKey`是必须的。配置命令时必须遵守下面的规则。当然你可以实现自己的解析器。

```java
 * 	命令对象规则
 *  1、一个命令分为三个部分：
 *   命令名 、 命令参数 、 命令值
 *  2、命令名是必须的，其他的可以为空
 *  3、命令的参数不能是必须的
 *  4、命令的值可以多个
```



### 三）、Command

​	命令对象

```java
 	/**
     * 命令的名字
     */
    private String key;

    /**
     * 命令参数
     * 参数名： 参数值
     */
    private Map<String, String> params;

    /**
     * 命令的值
     */
    private List<String> valueList;

```

​	该对象表示一个解析好后的对象



### 四）、CommandParseHandler

​	命令解析器，用于解析命令

```java
  /**
     * 解析命令
     * @param commandStr 要执行的命令
     * @param commandConfig 命令的配置对象
     * @return 返回一个命令对象
     * @exception ParseException 解释错误会出现解析错误
     */
    public Command parse(@NonNull String commandStr,
                         @NonNull CommandConfig commandConfig) 
        				throws ParseException;
```

​	默认通过空格拆分来解析命令，可自行扩展





### 五）、CommandExecutor

​	命令执行器，解析好命令后会通过执行器执行命令。可自行扩展

```java
 /**
     * 执行命令
     * @param command 执行的命令
     * @param commandJob 执行的任务
     * @exception ExecuteException 命令执行失败异常
     */
    public void execute(Command command, CommandJob commandJob) 
        				throws ExecuteException;
```



### 六）、CommandDto

​	命令传输对象，用来做命令的持久化，可自行扩展，默认会将命令持久化到json文件，运行时会把命令加载到内存中

```java

    /**
     * 加载所有工作
     * @param packagePath 指定路径，可以多个，如果为空则全盘扫描
     * @return 返回工作数据
     */
    public Map<String, Map<String,CommandJob>> load(String... packagePath);

    /**
     * 重新加载工作
     * @param packagePath 指定路径，可以多个，如果为空则全盘扫描
     * @return 返回工作数据
     */
    public Map<String, Map<String,CommandJob>> reload(String... packagePath);

    /**
     * 添加命令，可以多个
     * @param commandJobList 命令列表
     * @return 成功返回命令列表
     */
    public List<CommandJob> add(List<CommandJob> commandJobList);

    /**
     * 获得工作列表
     * @param group 组名
     * @return 返回工作列表
     */
    public Collection<CommandJob> getCommandJobList(String group);


    /**
     * 获得指定的工作对象
     * @param commandKey 命令名
     * @return 返回工作对象
     */
    public CommandJob getCommandJob(String commandKey);
```



#### 1、命令数据的数据结构

​	命令的数据结构是这样的`Map<String, Map<String,CommandJob>>`

​	解析如下：

```java
key : groupName
value:
	key : commandKey
	value: CommandJob对象
```



### 七）、CommandManager

​	顾名思义，命令管理器，它是一个大管家。命令的执行从这里开始

​	它包含系统配置对象、命令传输对象（[CommandDto](#六）CommandDto)）以及终端对象（[Terminal](#1Terminal)）等





### 八）、GUI相关

​	2.0版本以后使用了窗体的界面，这样会有更好的交互，同样也会有很多新的问题

​	`2.0`是个不稳定的版本，要使用的话请选择`2.1.1`

​		

#### 1、Terminal

​	一个抽象话的终端接口，个人实力有限，对gui并不了解。目前默认终端只是一个**文本域**。这样会有以下问题：

- 光标可以随意乱飞，这样会打乱命令输入。目前是锁死了光标永远在文本最后一个字符后面。但这样会导致方向键无法控制光标位置
- 文本域不支持滚动输入

```java

/**
 * 终端接口
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface Terminal {


    /**
     * 终端上打印
     *
     * @param message 打印的信息
     */
    public void print(String message);

    /**
     * 终端上换行打印
     *
     * @param message 打印的消息
     */
    public void println(String message);

    /**
     * 清空打印
     */
    public void clear();

    /**
     * 获得终端上的文本
     */
    public String getText();

    /**
     * 在终端上设置文本
     * @param text 文本
     */
    public void setText(String text);

    /**
     * 获得键盘监听处理器管理器
     *
     * @return 返回键盘监听处理器列表
     */
    public default KeyListenerHandlerManage getKeyListenerHandlerManage() {
        return Singleton.get(KeyListenerHandlerManage.class);
    }


    /**
     * 获得命令上下文
     *
     * @return 返回命令上下文
     */
    public default CommandContent getCommandContent() {
        return Singleton.get(DefaultCommandContent.class);
    }

    /**
     * 获得历史命令管理器
     * @return 返回历史列表
     */
    public default CommandHistoryManage getHistoryCommandManage() {
        return Singleton.get(CommandHistoryManage.class);
    }



}

```



#### 2、KeyListenerHandler

​	按键监听处理器，我希望在扩展的时候不需要修改核心逻辑，所以对于键盘监听抽象出来，只需要实现这个处理器就能实现对某个按键

的监听。

​	为了系统正常运行，请不要修改系统自带一下键盘监听处理器。如：`EnterHandler`、`BackspaceHandler`的监听

​	**注意**：实现该接口的类必须要在`guhong.play.commandsystem.gui.key`包下。

```java

/**
 * 键盘监听处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface KeyListenerHandler {

    /**
     * 是否监听
     * @param e 事件对象
     * @return 监听返回true
     */
    public boolean isListener(KeyEvent e);

   /**
     * 监听类型
     * 详情请见枚举： KeyType
     */
    public default KeyType type() {
        return KeyType.NOT_PRINT;
    }

    /**
     * 执行
     * @param terminal 终端对象
     */
    public void execute(Terminal terminal);

}

```



#### 3、CommandContent

​	这是一个命令的上下文，既在终端中命令的内容。这是为了更加方便获取命令的内容而抽象出来的。你就可

以把它看做是一个命令字符串，终端上输入的命令就是这个对象。

​	同样，这也是个接口，默认情况下时候`StringBuffer`来存储。

```java

/**
 * 命令上下文对象
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface CommandContent {

    /**
     * 追加
     * @param str 命令字符串
     */
    public void append(String str);

    /**
     * 删除一个字符串
     */
    public void delete();

    /**
     * 清空命令
     */
    public void clear();


    /**
     * 获得命令字符串
     * @return 返回命令字符串
     */
    public String getCommandStr();
}

```



#### 4、CommandHistoryManage

​	命令历史管理器，用于记录命令。有了这个之后，可以通过方向键来切换历史命令了。

​	**注：该模块在`2.1.2`才引入。**

​	

​	

### 九）、CommandMode

​	命令模式。即控制默认情况下会以什么样的方式去执行命令。简单来说就是在命令找不到时，使用什么方式去处理输入的命令。	

​	主要是为了更加方便 of 命令的使用，现在只需要在命令模式为“OF”的情况下，直接在终端上输入想打开的文

件就可以快速打开。也可以使用`mode [模式名] `来切换。详细用法可以使用`help mode`查看帮助

​	**注：**

- **该机制在`2.1.3`版本引入。**
- 该机制还无法进行扩展.





## 七、自定义命令	

​	终于来到了这里，了解了上面的介绍，自定义命令将变得非常简单



### 一）、步骤

**1、下载源码到本地**

​	略

**2、继承CommandJob**

​	打开项目，找个地方创建一个新class，然后让其继承`CommandJob`接口。

**3、reload**

​	测试好自己写的CommandJob的后，运行项目。然后输入`reload`命令重新加载命令，此时你就可以使用

`list`命令查看到你自定义的命令了

**4、打包项目**

​	确定命令没问题后，在终端上输入`build`命令，系统将自动帮你打包当前项目。打包后就可以使用

`项目路径/start/start.bat`启动你的命令系统了。



### 二）、例子

**1、下载源码到本地**

​	略

**2、继承CommandJob**

```java


/**
 * 一个用于测试命令的工作
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CommandTestJob implements CommandJob {

    /**
     * 这个方法配置命令的一些命令
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        // 设置命令的名字
        String commandKey = "test";
        // 设置命令的参数
        Map<String, Boolean> paramConfig = CollectionUtil.newHashMap();
        // 表示 -a 参数必须有一个值
        paramConfig.put("-a", true);
        // 表示 -f 参数的值可以为空
        paramConfig.put("-f", false);
        CommandConfig commandConfig = new SystemCommandConfig(commandKey, paramConfig);
        // 设置命令的介绍
        commandConfig.setDescription("这是一个测试命令");
        return commandConfig;

    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        // 这里写命令要执行的工作，也就是要做什么事情

        // 这里，我们打印出命令的名字、参数和值
        PrintUtil.println("commandKey："+command.getKey());
        Map<String, String> params = command.getParams();
        PrintUtil.println("commandParam: ");
        for (String key : params.keySet()) {
            PrintUtil.println("key: "+key + " | value: " + params.get(key) );
        }
        PrintUtil.println("commandValue: "+ command.getValueList());
    }
}

```



**3、reload**

​	运行项目，此时输入`list`命令还无法看到我们刚才自定义的命令。所以我们输入`reload`命令重新加载命令

```javacommandsystem


guhong#2021-01-05 22-21 java-command-system/ : list

//......无法看到我们刚才自定义的命令

guhong#2021-01-05 22-21 java-command-system/ : reload
重新加载成功，使用[list]命令查看当前所有命令信息

guhong#2021-01-05 22-21 java-command-system/ : list
----------------------------------------------------------------------------------------------------
| 命令名 | 所属组 | 命令描述 | 执行命令的类 | 
----------------------------------------------------------------------------------------------------
// ....

// 这里就出现了我们刚才自定义的命令
| test | system | 这是一个测试命令 | class guhong.play.commandsystem.job.system.job.CommandTestJob | 

// .....
----------------------------------------------------------------------------------------------------

guhong#2021-01-05 22-21 java-command-system/ : 
```



**4、打包项目**

​	最后输入`build`命令，系统将自动帮你打包当前项目。

```javacommandsystem
guhong#2021-01-05 22-21 java-command-system/ : build
// 等待一会。出现"打包完成"则表示打包成功
```






## X、以后的计划

- 新增

    - of 命令扩展
        - 忽略文件支持删除
        - 快捷目录支持删除
        - 更方便的忽略文件

            - 支持通过忽略文件进行忽略
            - 增加一些默认的忽略标识。如：`.jpg  .png .git`...
        - of支持路径匹配
        
            

    - 支持扩展终端
    
    - 支持别名
    
    - 支持管道符
    
    - 支持一次性执行多命令
    
    - 支持tab命令补全/提示
- 优化

    - 优化终端显示
        - 解决光标定位问题
        - 解决控制台无法动态打印问题。
    - 保证只运行一个程序的同时，弹出或者高亮已经在运行的程序
    - build 命令
        - 构建项目后，可以选择是否创建快捷方式到桌面
    - 优化命令传输对象，维护一个CommandKey列表：List<String> 来满足特定业务的需求
- bug

    - 



