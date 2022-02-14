package us.fjj.spring.learning.conditionalannotationusage.test4;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Conditional(EnvironmentCondition.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnvironmentConditional {
    enum ENVIRONMENT {
        TEST,
        DEVELOPING,
        PRODUCT
    }

    ENVIRONMENT value() default ENVIRONMENT.DEVELOPING;
}
