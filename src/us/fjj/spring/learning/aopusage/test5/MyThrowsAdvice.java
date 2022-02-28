package us.fjj.spring.learning.aopusage.test5;

import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MyThrowsAdvice implements ThrowsAdvice {
    public void afterThrowing(Exception ex) {
        System.out.printf("抛出异常的原因：%s\n", ex.getMessage());
        ex.printStackTrace();
    }

    /**
     * 最后一个afterThrowing优先
     */
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) {
        System.out.printf("抛出异常的方法：%s\n", method.getName());
        System.out.printf("抛出异常的方法参数：%s\n", Arrays.stream(args).collect(Collectors.toList()));
        System.out.printf("抛出异常的目标对象：%s\n", target);
        System.out.printf("抛出异常的原因：%s\n", ex.getMessage());
        ex.printStackTrace();
    }
}
