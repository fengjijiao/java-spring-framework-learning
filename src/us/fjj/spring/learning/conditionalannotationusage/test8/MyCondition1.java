package us.fjj.spring.learning.conditionalannotationusage.test8;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MyCondition1 implements Condition {
    /**
     * 判断容器中是否存在Service类型的bean。
     * @param context
     * @param metadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //获取spring容器
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //判断容器中是否存在Service类型的bean
        boolean existsService = !beanFactory.getBeansOfType(Service.class).isEmpty();
        return existsService;
    }
}
