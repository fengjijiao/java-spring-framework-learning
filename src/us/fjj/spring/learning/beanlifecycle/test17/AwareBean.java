package us.fjj.spring.learning.beanlifecycle.test17;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;

public class AwareBean implements BeanNameAware, BeanClassLoaderAware, BeanFactoryAware {
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println(String.format("setBeanClassLoader(%s)", classLoader));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println(String.format("setBeanFactory(%s)", beanFactory));
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(String.format("setBeanName(%s)", name));
    }
}
