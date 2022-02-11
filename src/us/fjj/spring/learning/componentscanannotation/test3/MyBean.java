package us.fjj.spring.learning.componentscanannotation.test3;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component//@1
public @interface MyBean {
    @AliasFor(annotation = Component.class)//@2
    String value() default "";//@3
}
//@1~@3使自定义注解支持定义bean名称
