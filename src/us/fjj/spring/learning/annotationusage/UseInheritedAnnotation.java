package us.fjj.spring.learning.annotationusage;

import java.lang.annotation.*;

//@Inherited：实现类之间的注解继承
/**
 * 作用：让子类可以继承父类中被@Inherited修饰的注解，注意是继承父类中的，如果接口中的注解也使用@Inherited修饰了，那么接口的实现类是无法继承这个注解的。
 */
public class UseInheritedAnnotation {
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface A1 {
        String value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface A2 {
        String value();
    }

    @A1("i am in interface.")
    interface I1 {
    }

    @A2("i am in class.")
    static class C1 {
    }

    static class C2 extends C1 implements I1 {
    }

    public static void main(String[] args) {
        for (Annotation annotation:
                C2.class.getAnnotations()) {
            System.out.println(annotation);
        }
        /**
         * 在没有在A1、A2上添加@Inherited注解时，C2类上没有注解
         * 添加后的结果如下：
         * @us.fjj.learning.annotation.UseInheritedAnnotation$A2("i am in class.")
         */
    }
}
