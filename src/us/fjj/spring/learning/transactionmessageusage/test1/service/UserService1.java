package us.fjj.spring.learning.transactionmessageusage.test1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.fjj.spring.learning.transactionmessageusage.test1.MsgSender;

@Component
public class UserService1 {
    @Autowired
    private UserService userService;
    @Autowired
    private MsgSender msgSender;

    //嵌套事务案例
    @Transactional
    public void nested() {
        this.msgSender.send("消息1", 2, "1");
        //registerRequiresNew事务传播属性是REQUIRES_NEW,会在一个新事务中运行
        this.userService.registerRequiresNew(1L, "张三");
        //registerFail事务传播属性是默认的，会在当前事务中运行，registerFail弹出异常会导致当前事务回滚
        this.userService.registerFail(2L, "李四");
    }
    /**
     * nested是外围方法，这个方法上有@Transactional，运行的时候会开启一个事务，内部3行代码：
     * @1： 发送消息，会在当前事务中执行
     * @2: registerRequiresNew事务传播行为是REQUIRES_NEW，所以会重新启用一个事务
     * @3: registerFail事务传播行为是默认的REQUIRED，会参与到nested()开启的事务中运行，registerFail方法内部会抛出一个异常，最终导致外部方法事务回滚。
     * 上面方法需要投递3条消息，而@1和@3投递的消息由于事务回滚导致消息被回滚，而@2在独立的事务中执行，@2的消息会投递成功，下面来看看执行结果，是不是和分析的一致。
     */
}
