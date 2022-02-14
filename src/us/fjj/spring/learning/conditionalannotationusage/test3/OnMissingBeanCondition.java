package us.fjj.spring.learning.conditionalannotationusage.test3;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class OnMissingBeanCondition implements Condition {
    /**
     * 判断容器中是否存在IService类型的bean，不存在的时候返回true
     * @param context
     * @param metadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //获取bean工厂
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //从容器中获取IService类型bean
        Map<String, IService> serviceMap = beanFactory.getBeansOfType(IService.class);
        //判断serviceMap是否为空
        return serviceMap.isEmpty();
    }
}
