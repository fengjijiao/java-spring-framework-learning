package us.fjj.spring.learning.enableasynandasynccusage.test5;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LogService5 {
    @Async
    public void mockNoReturnException() {
        //模拟抛出一个异常
        throw new IllegalArgumentException("无返回值的异常!");
    }
}
