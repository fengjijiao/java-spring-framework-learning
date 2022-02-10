package us.fjj.spring.learning.annotationusage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {
        ElementType.TYPE_PARAMETER
})//用来标注类型参数，类型参数一般在类后面声明或者方法上声明
@Retention(RetentionPolicy.RUNTIME)
public @interface Ann7 {
    String value();
}
