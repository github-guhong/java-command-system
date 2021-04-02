## 一、项目作用

​	当你写代码有一定时间了，你会发现，你的手不太愿意离开键盘去碰鼠标，即便你是使用笔记本的触摸板

你也不愿意去用鼠标去操作，此时你终于明白`linux`是多么舒服，用命令几乎可以完成所有事，但无奈windows

的cmd不会操作，又不想学，最后接受现实，还是要用鼠标去点击各种图标

​	如果你有上面的苦恼，Java程序员们有福了，只要你下载本项目，就可以实现你的梦想。而且支持自定义命

令，各种骚操作等你实现





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
	file
		config.json 	// of命令相关配置
	command-data.json 	// 命令数据文件
	config.json			// 系统配置文件
document 				// 命令文档目录，存放命令的介绍
start 					// 启动文件目录
	config 				// 运行时的配置目录，和上面同理
	document			// 命令文档，同上
	lib 				// 打包好的项目地址
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



## 五、命令运行流程

```
初始化：
	读取配置——》加载命令传输对象（CommandDto）——》读取命令
执行命令时：
	输入命令——》验证是否有效——》命令解析器（CommandParseHandler）——》
	命令执行器（CommandExecutor）——》执行工作（CommandJob.run）
```





## 六、核心类介绍

### 一）、CommandJob

​	`CommandJob`是个接口。该接口包含4个方法

```java
 	/**
     * 获得命令的配置
     * @return 返回命令的配置
     */
    public CommandConfig getCommandConfig();



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

​	其中`getCommandConfig`和`run`方法必须实现,如有需要可自行扩展`getParseHandler`和`getExecutor`。

分别对应**命令解释器**和**命令执行器**。

​	`getCommandConfig`方法需要返回一个命令的配置对象，详情请见**CommandConfig**对象

​	`run`方法就是要执行命令后要运行的代码，该方法会传入一个解析好的**Command**对象



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

​	该对象用于配置命令，其中`commandKey`是必须的。配置命令时必须遵守下面的规则。

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

​	命令传输对象，用来做命令的持久化，可自行扩展，默认会将命令持久化到配置文件，运行时会把命令加载

到内存中

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



### 七）、命令数据的数据结构

​	命令的数据结构是这样的`Map<String, Map<String,CommandJob>>`

​	解析如下：

```java
key : groupName
value:
	key : commandKey
	value: CommandJob对象
```



### 八）、CommandManager

​	顾名思义，命令管理器，命令的执行从这里开始。

​	它包含系统配置对象、命令传输对象（[CommandDto](#六）、CommandDto)）以及终端对象（[Terminal]()）





### 九）、GUI相关

​	2.0版本使用了窗体的界面，这样会有更好的交互，同样也会有很多的问题，所以`2.0`是个不稳定的版本，要

使用的话请选择`2.1.0`

​		

#### 1、Terminal

​	一个抽象话的终端接口，个人实力优先，对gui并不了解，所以希望可以把终端这个概念抽离出来。

​	默认情况下使用`TextAreaTerminal`文本域作为终端页面。

```java

/**
 * 终端接口
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface Terminal {


    /**
     * 终端上打印
     * @param message 打印的信息
     */
    public void print(String message);

    /**
     * 终端上换行打印
     * @param message 打印的消息
     */
    public void println(String message);

    /**
     * 清空打印
     */
    public void clear();

    /**
     * 获得键盘监听处理器列表
     * @return 返回键盘监听处理器列表
     */
    public default KeyListenerHandlerList getKeyListenerHandlerList() {
        return Singleton.get(KeyListenerHandlerList.class);
    }


    /**
     * 获得命令上下文
     * @return 返回命令上下文
     */
    public default CommandContent getCommandContent() {
        return Singleton.get(DefaultCommandContent.class);
    }
}

```



#### 2、KeyListenerHandler

​	按键监听处理器，我希望在扩展的时候不需要修改核心逻辑，所以对于键盘监听抽象出来，只需要实现这个

处理器就能实现对某个按键的监听。

​	为了系统正常运行，请不要修改系统自带一下键盘监听处理器。如：`EnterHandler`、`BackspaceHandler`

的监听

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
     * 是否结束监听
     * @param e 事件对象
     * @param terminal 终端对象
     * @return 不监听返回true
     */
    public boolean isExit(KeyEvent e, Terminal terminal);

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

​	同样，这也是个接口，默认情况下时候`StringBuilder`来存储。

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



​	

​	



## 七、自定义命令	

​	终于来到了这里，了解了上面的介绍，自定义命令将变得非常简单



### 一）、步骤

**1、下载源码到本地**

​	略

**2、继承CommandJob**

​	正如上面所述的，继承`CommandJob`接口，然后定义好自己的命令和执行的内容。记得测试哦

**3、reload**

​	运行项目,输入`reload`命令重新加载命令，此时你就可以在开发工具中使用你自定义的命令了

**4、打包项目**

​	最后输入`build`命令，系统将自动帮你打包当前项目。



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








## 八、下个版本需求


- v2.1.1
- bug

    - build命令-p参数下无法正确拼接路径的问题 √
    - of命令参数空格导致匹配不正确问题 √
    - of无法正确打开特殊字符的文件 √
    - of命令添加多个快捷目录时会导致所有的索引失效问题 √
    - 解决粘贴的内容无法被捕获的问题 √

- 优化
    - of支持严格匹配 √
    - of支持路径匹配 

- 新增
    - 支持上下键查询命令历史 √
    - ctrl + shift + l 清空控制台 √
    - 支持组合快捷键 √




- v2.1.2
- 新增

    - system组增加命令
        - font 设置字体
        - bg 设置背景
        - 
    - of 命令扩展
        - 忽略文件支持删除

        - 快捷目录支持删除

        - 更方便的忽略文件

            - 支持通过忽略文件进行忽略
            - 增加一些默认的忽略标识。如：`.jpg  .png .git`...

        - 增加默认快捷路径，默认将start目录设置为快捷路径

    - 支持扩展终端
    - 支持别名
    - 
- 优化

    - 深度优化光标定位问题。
    - 支持实时打印
    - build 命令
        - 构建项目后，可以选择是否创建快捷方式到桌面
- bug

    - 




