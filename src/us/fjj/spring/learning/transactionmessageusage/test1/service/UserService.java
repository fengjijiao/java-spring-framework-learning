package us.fjj.spring.learning.transactionmessageusage.test1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.fjj.spring.learning.transactionmessageusage.test1.MsgSender;

@Component
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MsgSender msgSender;

    /**
     * 模拟用户注册成功，顺便发送消息
     */
    @Transactional
    public void register(Long user_id, String user_name) {
        //先插入用户
        this.jdbcTemplate.update("insert into t_user(id,name) value (?, ?)", user_id, user_name);
        System.out.println(String.format("用户注册: [user_id:%s,user_name:%s]", user_id, user_name));
        //发送消息
        String msg = String.format("[user_id:%s,user_name:%s]", user_id, user_name);
        //调用投递器的send方法投递消息
        this.msgSender.send(msg, 1, user_id.toString());
    }

    /**
     * 模拟用户注册失败，咱们可以通过弹出异常让事务回滚，结果也会导致消息发送被取消
     */
    @Transactional
    public void registerFail(Long user_id, String user_name) {
        this.register(user_id, user_name);
        throw new RuntimeException("故意失败！");
    }

    /**
     * 事务传播属性是REQUIRES_NEW,会在独立的事务中运行
     *
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerRequiresNew(Long user_id, String user_name) {
        this.register(user_id, user_name);
    }


























}
