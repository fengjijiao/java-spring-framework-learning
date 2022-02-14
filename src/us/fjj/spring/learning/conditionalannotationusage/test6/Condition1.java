package us.fjj.spring.learning.conditionalannotationusage.test6;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Order(1)
public class Condition1 implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        System.out.println(this.getClass().getSimpleName());
        return true;
    }
}
