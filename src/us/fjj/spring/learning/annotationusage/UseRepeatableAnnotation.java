package us.fjj.spring.learning.annotationusage;

//@Repeatable：重复使用注解

import java.lang.annotation.*;

/**
 * 在类上重复定义相同名称的注解会产生错误，例如：
 *
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target(ElementType.TYPE)
 * @interface Ann9{}
 * @Ann9
 * @Ann9 class X {}
 * 像上面这样如果我们想重复使用注解的时候，需要用到@Repeatable注解
 * <p>
 * 使用步骤：
 * 1.先定义容器注解
 * 2.为注解指定容器
 * 3.使用注解
 */
public class UseRepeatableAnnotation {
    //1.先定义容器注解(Ann10s)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    @interface Ann10s {
        Ann10[] value();//容器注解中必须要有个value类型的参数，参数类型为子注解(Ann10)类型的数组。
    }

    //2.为注解指定容器
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    @Repeatable(Ann10s.class)//要让一个注解可以重复使用，需要在注解上加上@Repeatable注解，@Repeatable中的value的值为容器注解
    @interface Ann10 {
        String value();
    }

    //3.使用注解

    /**
     * 重复使用相同的注解有两种方式，如下:
     * 1.重复使用注解，如下面的类上重复使用@Ann10注解
     * 2,通过容器注解来使用更多个注解，如下面的字段v1上使用@Ann10s容器注解
     */
    @Ann10("ps 1")
    @Ann10("ps 2")
    static class C1 {
        @Ann10s({
                @Ann10("v ps 1"),
                @Ann10("v ps 2")
        })
        String v1;
    }

    //获取注解信息
    public static void main(String[] args) throws NoSuchFieldException {
        for (Annotation annotation :
                C1.class.getAnnotations()) {
            System.out.println(annotation);
        }
        for (Annotation annotation :
                C1.class.getDeclaredField("v1").getAnnotations()) {
            System.out.println(annotation);
        }
        /**
         * @us.fjj.learning.annotation.UseRepeatableAnnotation$Ann10s({@us.fjj.learning.annotation.UseRepeatableAnnotation$Ann10("ps 1"), @us.fjj.learning.annotation.UseRepeatableAnnotation$Ann10("ps 2")})
         * @us.fjj.learning.annotation.UseRepeatableAnnotation$Ann10s({@us.fjj.learning.annotation.UseRepeatableAnnotation$Ann10("v ps 1"), @us.fjj.learning.annotation.UseRepeatableAnnotation$Ann10("v ps 2")})
         */
    }
}
