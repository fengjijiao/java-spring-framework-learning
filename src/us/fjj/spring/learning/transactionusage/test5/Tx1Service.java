package us.fjj.spring.learning.transactionusage.test5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Tx1Service {
    @Autowired
    private Ds1User1Service ds1User1Service;
    @Autowired
    private Ds1User2Service ds1User2Service;
    @Autowired
    private Ds2User1Service ds2User1Service;
    @Autowired
    private Ds2User2Service ds2User2Service;

    @Autowired
    private Tx2Service tx2Service;

    /**
     * 场景1
     * 外围方法和内部方法使用相同的事务管理器，传播行为都是REQUIRED。
     *
     */
    @Transactional(transactionManager = "transactionManager1", propagation = Propagation.REQUIRED)
    public void test1() {
        this.ds1User1Service.required("张三");
        this.ds1User2Service.required("李四");
        throw new RuntimeException();
    }
    /**
     * 场景2
     * 外部方法和内部方法使用不同的事务管理器
     *
     */
    @Transactional(transactionManager = "transactionManager2", propagation = Propagation.REQUIRED)
    public void test2() {
        this.ds1User1Service.required("张三");
        this.ds1User2Service.required("李四");
        throw new RuntimeException();
    }
    /**
     * 场景3
     *
     */
    @Transactional(transactionManager = "transactionManager1", propagation = Propagation.REQUIRED)
    public void test3() {
        this.ds1User1Service.required("张三");
        this.ds1User2Service.required("李四");
        this.ds2User1Service.required("王五");
        this.ds2User1Service.required("赵六");
        throw new RuntimeException();
    }
    /**
     * 场景4
     * TX2和TX1中都有
     */
    @Transactional(transactionManager = "transactionManager1", propagation = Propagation.REQUIRED)
    public void test4() {
        this.ds2User1Service.required("张三");
        this.ds2User2Service.required("李四");
        this.tx2Service.test1();
        throw new RuntimeException();
    }
    /**
     * 场景5
     * TX2和TX1。。
     */
    @Transactional(transactionManager = "transactionManager1", propagation = Propagation.REQUIRED)
    public void test5() {
        this.ds1User1Service.required("张三");
        this.ds1User2Service.required("李四");
        this.tx2Service.test2();
    }































}
