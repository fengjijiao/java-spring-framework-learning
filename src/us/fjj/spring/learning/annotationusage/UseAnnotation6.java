package us.fjj.spring.learning.annotationusage;

import java.lang.annotation.ElementType;

/**
 * 演示自定义注解在类、字段、构造器、方法参数、方法、本地变量上的使用，@Ann6注解有个elementType参数，我想通过这个参数的值来告诉大家对应@Target中的那个值来限制使用目标。
 */
@Ann6(value = "我用在类上", elementType = ElementType.TYPE)
public class UseAnnotation6 {
    @Ann6(value = "我用在字段上", elementType = ElementType.FIELD)
    private String a;

    @Ann6(value = "我用在构造方法上", elementType = ElementType.CONSTRUCTOR)
    public UseAnnotation6(@Ann6(value = "我用在方法参数上", elementType = ElementType.PARAMETER) String a) {
        this.a = a;
    }

    @Ann6(value = "我用在普通方法上", elementType = ElementType.METHOD)
    public void m1() {
        @Ann6(value = "我用在了本地变量上", elementType = ElementType.LOCAL_VARIABLE) String b;
        System.out.println(a);
    }
}
