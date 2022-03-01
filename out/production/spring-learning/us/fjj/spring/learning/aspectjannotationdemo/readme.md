# 说明
在Spring中，尽管使用XML配置文件可以实现AOP开发，但是如果所有的相关配置都集中在配置文件中，势必会导致XML配置文件过于臃肿，从而给维护和升级带来一定的困难。  
为此，AspectJ框架为AOP开发提供了一套注解。AspectJ允许使用注解定义切面、切入点和增强处理，Spring框架将会根据这些注解生成AOP代理。  
# 启用@AspectJ注解有以下两种方式
## 1) 使用@Configuration和@EnableAspectJAutoProxy注解
```aidl
@Configuration
@EnableAspectJAutoProxy
public class Appconfig {
}
```
## 2) 基于XML配置
`<aop:aspectj-autoproxy>`