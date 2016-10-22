# **fact-async 1.3.0**

## **当前版本**

1.3.0-SNAPSHOT

## **Maven依赖**

```xml

<dependency>

<groupId>com.woter.fact</groupId>

<artifactId>fact-async</artifactId>

<version>1.3.0-SNAPSHOT</version>

</dependency>

```

## **功能描述**

fact-async 是一个基于Spring的异步并行框架；主要包括一下几个方面的功能，具体如下：

1、提供注解声明方式异步执行，对原代码无侵入（解决spring-async对有返回结果的需包装成Future对象问题）；

2、提供编程式异步方法；

3、提供异步事件编程；

4、解决多层异步嵌套带来的线程阻塞问题（目前spring-async依然存在此问题）；


## **举个栗子**

1、最典型的就是分页：

	通常我们都是串行执行两条SQL语句，一条查询列表数据一条查询count；比如查询列表数据耗时300ms查询count500ms，
	正常情况耗时则800ms;如果使用异步则只要500ms；

2、非事务性异步处理：

	某些情况下我在做完事务操作后需要操作一些耗时的非事务性处理，那这个时候则可以使用异步去完成提升了接口速度；


## **集成Spring配置说明**
1、Spring配置文件添加（必填）：

```<context:component-scan base-package="com.woter.fact.async" />```

2、Properties文件添加（注：都非必填，默认框架会自动设置）：
```
  #核心线程数(默认CPU核数*4)
  async.corePoolSize=8
  #最大线程数
  async.maxPoolSize=24
  #最大队列size
  async.maxAcceptCount=100
  #线程空闲时间
  async.keepAliveTime=10000
  #拒绝服务处理方式 (不建议修改)
  async.rejectedExecutionHandler=CALLERRUN
  #是否允许线程自动超时销毁(不建议修改)
  async.allowCoreThreadTimeout=ture
```
## **常用功能代码演示**

### **示例1 添加@Async注解**
```java
@Async(timeout=1000)
public List<InsureSimple> queryByCombinationGroup(ClaimsSearchRequest param, int pageNum, int pageSize) {
	return relationMapper.selectByCombinationGroup(param,pageStart,pageSize);
}
```

### **示例2 编程式异步**
```java
 public void insertSysLogBySrpingProxy(){
	TSystemLog systemLog = new TSystemLog();
	systemLog.setBusinessNo("20160406003137");
	systemLog.setLogContent("测试");
	systemLog.setLogType(4);
	systemLog.setOperatorId(3068l);
	systemLog.setOperatorName("张三");
	long startTime = System.currentTimeMillis();
	ISystemLogService systemLogServiceProsy = AsyncTemplate.buildProxy(systemLogService,1080);
	try{
	    User  user = systemLogServiceProsy.saveSystemLog(systemLog);
     }catch(Exception e){
	    logger.error(e.getMessage(),e);
     }
 }
```

### **示例3 异步事件编程**
```java
public void  addUser(User user){
        Map<String,User> map = new HashMap<String,User>();
        map.put("user",user);
	AsyncTemplate.execute(new AsyncTaskFuture<User>() {

	    @Override
            public User doAsync(Map<String,Object> dataMap) {

	        return (User)dataMap.get("user");
            }
	}, new AsyncFutureCallback<User>() {

	    @Override
	    public void onSuccess(User result) {
		logger.debug("调用成功" + result);
	    }

	    @Override
	    public void onFailure(Throwable t) {
		logger.debug("调用失败" + t);
	    }
	});
}
```

## **兼容性说明**

未发现异常
