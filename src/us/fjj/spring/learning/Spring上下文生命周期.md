# Spring上下文生命周期
## 1. 什么是spring应用上下文？
接口org.springframework.context.ApplicationContext表示Spring上下文，下面是它的2个实现类：
```text
org.springframework.context.support.ClassPathXmlApplicationContext
org.springframework.context.annotation.AnnotationConfigApplicationContext
```
## 2. 应用上下文生命周期（14个阶段）
1. 创建Spring应用上下文
2. 上下文启动准备阶段
3. BeanFactory创建阶段
4. BeanFactory准备阶段
5. BeanFactory后置处理阶段
6. BeanFactory注册BeanPostProcessor阶段
7. 初始化内建Bean: MessageSource（国际化）
8. 初始化内建Bean: Spring事件广播器
9. Spring应用上下文刷新阶段
10. Spring事件监听器注册阶段
11. 单例Bean实例化阶段
12. BeanFactory初始化完成阶段
13. Spring应用上下文启动完成阶段
14. Spring应用上下文关闭阶段

## 3. Spring应用上下文的使用
下面这段代码，是Spring上下文最常见的用法，稍后我们以这段代码为例，结合Spring源码，来看看每个阶段的细节。

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class MainConfig {
    public static void main(String[] args) {
        //1.创建Spring上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //2.上下文中注册bean
        context.register(MainConfig.class);
        //3.刷新Spring上下文。内部会启动Spring上下文
        context.refresh();
        //4.关闭Spring上下文
        System.out.println("stop ok!");
    }
}
```
......