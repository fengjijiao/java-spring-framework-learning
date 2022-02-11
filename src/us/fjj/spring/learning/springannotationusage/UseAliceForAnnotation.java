package us.fjj.spring.learning.springannotationusage;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class UseAliceForAnnotation {
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface A1 {
        String value() default "A1";
        String name() default "A1 name";
    }
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @A1()//important
    @interface A2 {
        String value();
        @AliasFor(annotation = A1.class, value = "name")//将a1Value与A1的name关联创建别名。当设置a1Value的值时会同步修改A1.name的值（前提是在A2上有@A1注解）。
        String a1Value();
    }
    @A2(value = "i am from Ires.", a1Value = "i am from Ires")
    static class Ires {
    }

    @Target({ElementType.TYPE, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface A3 {
        @AliasFor("v2")//当不设置annotation时默认为当前annotation
        String v1() default "";
        @AliasFor("v1")
        String v2() default "";
    }

    /**
     * 上面的代码，A3注解中的2个参数都设置了@AliceFor，@AliceFor如果不指定annotation参数的值，那么annotation默认值就是当前注解，所以上面的2个参数互为别名，当给v1设置值的时候也相当于给v2设置值，当给v2设置值的时候也相当于给v1设置值。
     *
     */

    @A3(v1 = "i am from Ires2.")
    static class Ires2 {
        @A3(v2 = "i am from name field.")
        private String name;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @A1
    @interface A4 {
        @AliasFor(annotation = A1.class)
        String name();
    }

    @A4(name = "i am from Ires3.")
    static class Ires3 {
    }
}
