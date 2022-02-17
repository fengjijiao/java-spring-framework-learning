package us.fjj.spring.learning.beanlifecycle.test23;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

public class MyDestructionAwareBeanPostProcessor implements DestructionAwareBeanPostProcessor {
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        System.out.println("准备销毁"+beanName);
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return true;
    }
}
