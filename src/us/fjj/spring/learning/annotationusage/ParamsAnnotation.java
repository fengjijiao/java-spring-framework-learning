package us.fjj.spring.learning.annotationusage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//多参数的注解类

/**
 * There are the following constants:
 * <p>
 * ANNOTATION_TYPE - Annotation type declaration
 * CONSTRUCTOR - Constructor declaration
 * FIELD - Field declaration (includes enum constants)
 * LOCAL_VARIABLE - Local variable declaration
 * METHOD - Method declaration
 * PACKAGE - Package declaration
 * PARAMETER - Parameter declaration
 * TYPE - Class, interface (including annotation type), or enum declaration
 */

/**
 * Let's say the annotation to which you specify the ElementType is called YourAnnotation:
 *
 * ANNOTATION_TYPE - Annotation type declaration. Note: This goes on other annotations
 *
 * @YourAnnotation
 * public @interface AnotherAnnotation {..}
 * CONSTRUCTOR - Constructor declaration
 *
 * public class SomeClass {
 *     @YourAnnotation
 *     public SomeClass() {..}
 * }
 * FIELD - Field declaration (includes enum constants)
 *
 * @YourAnnotation
 * private String someField;
 * LOCAL_VARIABLE - Local variable declaration. Note: This can't be read at runtime, so it is used only for compile-time things, like the @SuppressWarnings annotation.
 *
 * public void someMethod() {
 *     @YourAnnotation int a = 0;
 * }
 * METHOD - Method declaration
 *
 * @YourAnnotation
 * public void someMethod() {..}
 * PACKAGE - Package declaration. Note: This can be used only in package-info.java.
 *
 * @YourAnnotation
 * package org.yourcompany.somepackage;
 * PARAMETER - Parameter declaration
 *
 * public void someMethod(@YourAnnotation param) {..}
 * TYPE - Class, interface (including annotation type), or enum declaration
 *
 * @YourAnnotation
 * public class SomeClass {..}
 * You can specify multiple ElementTypes for a given annotation. E.g.:
 *
 * @Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
 *
 * https://stackoverflow.com/questions/3550292/what-do-java-annotation-elementtype-constants-mean#:~:text=ElementType%20%3A,to%20use%20an%20annotation%20type.
 */
//指定注解使用范围
@Target(value = {ElementType.TYPE, ElementType.METHOD})
//指定了ParamsAnnotation注解可以用在类、接口、注解类型、枚举类型以及方法上面，自定义注解上也可不使用@Target注解，如果不使用，表示自定义注解可以用在任何地方。


//指定注解的保留策略
/**
 * java程序的3个过程：1.源码阶段,2,源码被编译为字节码之后变成class文件,3.字节码被虚拟机加载然后运行
 * 自定义注解可以通过@Rotention注解来指定保留在上面哪个阶段
 */
@Retention(RetentionPolicy.SOURCE)
/**
 * RetentionPolicy.SOURCE：注解只保留在源码中，编译为字节码后就丢失了，也就是class文件中就不存在了
 * RetentionPolicy.CLASS：注解只保留在源码和字节码中，运行阶段会丢失
 * RetentionPolicy.RUNTIME：源码、字节码、运行期间都存在
 */
//上面的注解指定了ParamsAnnotation只存在于源码阶段
public @interface ParamsAnnotation {
    public int param1() default 0;

    int param2() default 0;

    //上面两行等效
    boolean param3() default false;

    String[] param4();
    String[] param5() default {"w", "q"};
    /**
     * 注解可以定义多个参数，参数的定义有以下特点：
     * 1.访问修饰符必须为public，不写则默认为public
     * 2.该元素的类型只能是基本数据类型、String、Class、枚举类型、注解类型（体现了注解的嵌套效果）以及上述类型的一维数组
     * 3.该元素的名称一般定义为名词，如果注解中只有一个元素，请把名字起为value(后面使用会带来便利操作)
     * 4.参数名称后面的()不是定义方法参数的地方，也不能在括号中定义任何参数，仅仅只是一个特殊的语法
     * 5.default代表默认值，值必须和第2点定义的类型一致
     * 6.如果没有默认值，代表后续使用注解时必须给该类型元素赋值
     */
}
