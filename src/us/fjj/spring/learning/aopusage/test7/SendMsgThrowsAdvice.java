package us.fjj.spring.learning.aopusage.test7;

import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SendMsgThrowsAdvice implements ThrowsAdvice {
    public void afterThrowing(Method method, Object[] args, Object target, RuntimeException ex) {
        //监控到异常后发送消息通知开发者
        System.out.println("异常警报：");
        System.out.println(String.format("method: [%s], args: [%s]", method.getName(), Arrays.stream(args).collect(Collectors.toList())));
        System.out.println(ex.getMessage());
        System.out.println("请尽快修复bug!");
    }
}
