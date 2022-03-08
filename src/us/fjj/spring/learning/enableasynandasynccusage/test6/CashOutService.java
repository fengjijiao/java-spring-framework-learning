package us.fjj.spring.learning.enableasynandasynccusage.test6;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CashOutService {
    @Async(MainConfig6.CASHOUT_EXECUTION_BEAN_NAME)
    public void cashOut() {
        System.out.println(Thread.currentThread()+"模拟异步提现");
    }
}
