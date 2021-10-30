package us.fjj.spring.learning.springjdkdynamicproxy;

//MyAspect类在切面中定义了两个增强的方法，分别为myBefore和myAfter。
public class MyAspect {
    public void myBefore() {
        System.out.println("方法执行前");
    }
    public void myAfter() {
        System.out.println("方法执行后");
    }
}
