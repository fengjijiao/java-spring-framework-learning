package us.fjj.spring.learning.importannotationusage.test6;

import org.springframework.context.annotation.ComponentScan;

/**
 * 使用@ComponentScan注解，将Service1，Service2、Ncp3类注册到容器中
 * @1: 此处使用了@EnableMethodCostTime注解，而@EnableMethodCostTime注解上使用了
 * @Import(MethodCostTimeImportSelector.class)，此时
 * MethodCostTimeImportSelector类中的MethodCostTimeProxyBeanPostProcessor会被注册到容器，
 * 会拦截bean的创建，创建耗时代理对象。
 */
@ComponentScan
@EnableMethodCostTime//@1
public class MainConfig6 {
}
