package us.fjj.spring.learning.databaseseparateusage.base;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring配置类，作用
 * 1.@3: 用来将base包中的一些类注册到spring容器中，比如上面的拦截器ReadWriteInterceptor
 * 2.@1: 开启spring aop的功能
 * 3.@2: 开启spring自动管理事务的功能，@EnableTransactionManagement的order用来指定事务拦截器org.springframework.transaction.interceptor.TransactionInterceptor顺序，在这里我们将order设置为Integer.MAX_VALUE-1，而上面ReadWriteInterceptor的order是Integer.MAX_VALUE-2，所以ReadWriteInterceptor会在事务拦截器之前执行。
 *
 */
@Configuration
@EnableAspectJAutoProxy//@1
@EnableTransactionManagement(proxyTargetClass = true, order = Integer.MAX_VALUE - 1)//@2
@ComponentScan(basePackageClasses = IService.class)//@3
public class ReadWriteConfiguration {
}
