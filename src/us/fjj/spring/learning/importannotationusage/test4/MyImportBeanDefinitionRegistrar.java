package us.fjj.spring.learning.importannotationusage.test4;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * value为ImportBeanDefinitionRegistrar接口类型 用法
 * 1.定义ImportBeanDefinitionRegistrar接口实现类，在registerBeanDefinitions方法中使用registry来注册bean
 * 2.使用@Import来导入步骤1中定义的类
 * 3.使用步骤2中@Import标注的类作为AnnotationConfigApplicationContext构造参数创建spring容器
 * 4.使用AnnotationConfigApplicationContext操作bean
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //定义一个bean：Service1
        BeanDefinition service1BeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Service1.class).getBeanDefinition();
        //注册bean
        registry.registerBeanDefinition("service1", service1BeanDefinition);//beanName: service1


        //定义一个bean：Service2，通过addPropertyReference注入service1
        BeanDefinition service2BeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Service2.class)
                .addPropertyReference("service1", "service1")//name, beanName
                .getBeanDefinition();
        //注册bean
        registry.registerBeanDefinition("service2", service2BeanDefinition);

        /**
         * 上面定义2个bean，效果与下类似：
         * <bean id="service1" class="...test4.Service1" />
         * <bean id="service2" class="...test4.Service2">
         * <property name="service1" ref="service1"/>
         * </bean>
         */
    }
}
