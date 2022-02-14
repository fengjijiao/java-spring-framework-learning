package us.fjj.spring.learning.conditionalannotationusage.test4;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

public class EnvironmentCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //当前需要使用的环境 （配置）
        EnvironmentConditional.ENVIRONMENT currentEnvironment = EnvironmentConditional.ENVIRONMENT.DEVELOPING;
        //获取使用条件的类上的EnvironmentCondition注解中对应的环境
        EnvironmentConditional.ENVIRONMENT environment = (EnvironmentConditional.ENVIRONMENT) Objects.requireNonNull(metadata.getAllAnnotationAttributes(EnvironmentConditional.class.getName())).getFirst("value");//.get("value").get(0);
        return currentEnvironment.equals(environment);
    }
}
