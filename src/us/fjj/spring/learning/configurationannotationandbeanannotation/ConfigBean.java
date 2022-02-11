package us.fjj.spring.learning.configurationannotationandbeanannotation;

import org.springframework.context.annotation.Configuration;

/**
 * @Configuration 这个注解可以加在类上，让这个类的功能等同于一个bean xml配置文件
 * 使用步骤：
 * 1.在类上使用@Configuration注解
 * 2.通过AnnotationConfigApplicationContext容器来加载@Configuration注解修饰的类
 */
@Configuration
public class ConfigBean {
}

/**
 * 等同于
 * <?xml version="1.0" encoding="UTF-8"?>
 * <beans xmlns="http://www.springframework.org/schema/beans"
 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * xsi:schemaLocation="http://www.springframework.org/schema/beans
 * http://www.springframework.org/schema/beans/spring-beans-4.3.xsd">
 * </beans>
 */