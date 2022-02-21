package us.fjj.spring.learning.beanfactoryextensions.test2;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class MyBeanDefinitionRegistryPostProcessor2 implements BeanDefinitionRegistryPostProcessor, Ordered {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registry.registerBeanDefinition(
                "name",
                BeanDefinitionBuilder
                        .genericBeanDefinition(String.class)
                        .addConstructorArgValue("sb lk")
                        .getBeanDefinition()
        );
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
