package us.fjj.spring.learning.aspectpointcutusage.test8;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//@Inherited
public @interface Ann8 {
    String value() default "";
}
