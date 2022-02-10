package us.fjj.spring.learning.annotationusage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE_USE})//能用在任何类型名称(T)上
@Retention(RetentionPolicy.RUNTIME)
public @interface Ann8 {
    String value();
}
