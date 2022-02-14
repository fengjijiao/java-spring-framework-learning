package us.fjj.spring.learning.conditionalannotationusage.test6;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class Condition2 implements Condition, Ordered {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        System.out.println(this.getClass().getSimpleName());
        return true;
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
