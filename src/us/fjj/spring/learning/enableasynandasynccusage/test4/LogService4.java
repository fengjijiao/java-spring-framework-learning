package us.fjj.spring.learning.enableasynandasynccusage.test4;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class LogService4 {
    @Async
    public Future<String> mockException() {
        throw new IllegalArgumentException("参数有误！");
    }
}
