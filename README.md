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

​	下载clone项目到本地，然后进入`start`目录，运行`start.bat`脚本即可



## 四、目录结构介绍

### 一）、项目结构

```
config  				// 配置文件目录
	command-data.json 	// 命令数据文件
	config.json 		// 系统配置文件
document 				// 命令文档目录，存放命令的介绍
start 					// 启动文件目录
	config 				// 运行时的配置目录，和上面同理
		command-data.json
		config.json
	lib 				// 打包好的项目地址
		command-system-1.0-jar-with-dependencies.jar
	start.bat 			// 启动脚本
src 					// 源码目录
```



### 二）、源码结构

```
constant 	// 常量文件
dto			// 存放数据传输对象，用于加载命令，可扩展
execption	// 存放异常
executor	// 存放命令执行器，用于执行命令，可扩展
job			// 存放“工作”对象，工作既一个命令要执行的事情
	system	// 存放系统命令
parse		// 存放命令解析器，用于解析命令，可扩展
util		// 存放工具
```



## 五、运行流程

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



### 七、命令数据的数据结构

​	命令的数据结构是这样的`Map<String, Map<String,CommandJob>>`

​	解析如下：

```java
key : groupName
value:
	key : commandKey
	value: CommandJob对象
```





## 七、自定义命令	

​	终于来到了这里，了解了上面的介绍，自定义命令将变得非常简单



**1、下载源码到本地**

​	略

**2、继承CommandJob**

​	正如上面所述的，继承`CommandJob`接口，然后定义好自己的命令和执行的内容。记得测试哦

**3、打包项目**

​	先clean一下项目然后再打包

​	注意要使用`assembly`插件打包，以`IDEA`中为例：

```
点开maven工具栏——>Plugins——> assembly——>assembly:assembly
```

​	选择`assembly:assembly`执行就好

**4、复制到启动目录**

​	将打包好的jar文件（注意是名字后缀带.`...with-dependencies.jar`的jar文件）放入`start\lib`目录中

**5、reload**

​	最后使用`start.bat`脚本启动项目，输入`reload`命令重新记载即可。	






## 八、版本功能

- v1

- 优化默认的解析器 √

- 高效加载命令，通过配置文件 √

- 支持外部的文本介绍 √

- -----------------------------------------------------------------------------------------------------------

- v2
- 窗口模式
    - 支持快捷键
        - 缓存命令
        - table补全
    - 支持彩色字体
- 适配windows
- 支持别名
- 支持环境变量
- 完成`build`命令，快捷打包



## 