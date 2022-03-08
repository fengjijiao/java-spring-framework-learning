package us.fjj.spring.learning.enableasynandasynccusage.test6;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RechargeService {
    @Async(MainConfig6.RECHARGE_EXECUTION_BEAN_NAME)
    public void recharge() {
        System.out.println(Thread.currentThread()+"模拟异步充值");
    }
}
