package us.fjj.spring.learning.transactionusage.test4;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TxService {
    @Autowired
    private User1Service user1Service;
    @Autowired
    private User2Service user2Service;

    /**
     * 场景1 （1-1）
     * 外围方法没有事务，外围方法内部调用2个REQUIRED级别的事务方法。
     * 案例中都是在TxService的方法内去调用另外2个Service，所以TxService中的方法统称外围方法，另外2个service中的方法内部方法。
     * <p>
     * 验证方法1
     * TxService添加
     */
    @Test
    public void noTransactionExceptionRequired() {
        this.user1Service.required("张三");
        this.user2Service.required("李四");
        throw new RuntimeException();
    }

    /**
     * 验证方法2
     */
    public void noTransactionRequiredException() {
        this.user1Service.required("张三");
        this.user2Service.requiredException("李四");
    }


    /**
     * 场景2（1-2）
     * 外围方法开启事务（Propagation.REQUIRED），这个使用频率特别高.
     * 验证方法1
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionExceptionRequired() {
        user1Service.required("张三");
        user2Service.required("李四");
        throw new RuntimeException();
    }

    /**
     * 验证方法2
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionRequiredException() {
        user1Service.required("张三");
        user2Service.requiredException("李四");
    }

    /**
     * 验证方法3
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionRequiredExceptionTry() {
        user1Service.required("张三");
        try {
            user2Service.requiredException("李四");
        } catch (Exception ex) {
            System.out.println("方法回滚");
        }
    }


    /**
     * 场景1（2-1）
     * 外围方法没有事务
     * 验证方法1
     */
    public void noTransactionExceptionRequiresNew() {
        user1Service.requiresNew("张三");
        user2Service.requiresNew("李四");
        throw new RuntimeException();
    }

    /**
     * 验证方法2
     */
    public void noTransactionRequiredNewException() {
        user1Service.requiresNew("张三");
        user2Service.requiresNewException("李四");
    }


    /**
     * 场景2 （2-2）
     * 外围方法开启事务
     * 验证方法1
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionExceptionRequiredRequiresNew() {
        user1Service.required("张三");
        user2Service.requiresNew("李四");
        user2Service.requiresNew("王五");
        throw new RuntimeException();
    }

    /**
     * 验证方法2
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionRequiredRequiresNewException() {
        user1Service.required("张三");
        user2Service.requiresNew("李四");
        user2Service.requiresNewException("王五");
    }

    /**
     * 验证方法3
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionRequiredRequiresNewExceptionTry() {
        user1Service.required("张三");
        user2Service.requiresNew("李四");
        try {
            user2Service.requiresNewException("王五");
        } catch (Exception ex) {
            System.out.println("回滚");
        }
    }


    /**
     * 场景1 (3-1)
     * 外围方法没有事务。
     * 验证方法1
     */
    public void noTransactionExceptionNested() {
        user1Service.nested("张三");
        user2Service.nested("李四");
        throw new RuntimeException();
    }
    /**
     * 验证方法2
     */
    public void noTransactionNestedException() {
        user1Service.nested("张三");
        user2Service.nestedException("李四");
    }


    /**
     * 场景2（3-2）
     * 外围方法开启事务
     * 验证方法1
     */
    @Transactional
    public void transactionExceptionNested() {
        user1Service.nested("张三");
        user2Service.nested("李四");
        throw new RuntimeException();
    }

    /**
     * 验证方法2
     */
    @Transactional
    public void transactionNestedException() {
        user1Service.nested("张三");
        user2Service.nestedException("李四");
    }
    /**
     * 验证方法3
     *
     */
    @Transactional
    public void transactionNestedExceptionTry() {
        user1Service.nested("张三");
        try {
            user2Service.nestedException("李四");
        }catch (Exception e) {
            System.out.println("方法回滚");
        }
    }
























}
