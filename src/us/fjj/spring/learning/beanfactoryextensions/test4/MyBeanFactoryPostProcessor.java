package us.fjj.spring.learning.beanfactoryextensions.test4;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        UserModel user1 = beanFactory.getBean("user1", UserModel.class);
        UserModel user2 = beanFactory.getBean("user2", UserModel.class);
    }
}
