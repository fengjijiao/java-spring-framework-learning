package us.fjj.spring.learning.importannotationusage.test6;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MethodCostTimeProxyBeanPostProcessor implements BeanPostProcessor {
    /**
     * 在bean创建以后将bean替换为代理对象
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getName().toLowerCase().contains("service")) {
            return CostTimeProxy.createProxy(bean);
        } else {
            return bean;
        }
    }
}
