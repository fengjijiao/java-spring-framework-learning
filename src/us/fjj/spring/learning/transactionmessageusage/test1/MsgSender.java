package us.fjj.spring.learning.transactionmessageusage.test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import us.fjj.spring.learning.transactionmessageusage.test1.model.MsgOrderModel;
import us.fjj.spring.learning.transactionmessageusage.test1.service.MsgOrderService;
import us.fjj.spring.learning.transactionmessageusage.test1.service.MsgService;

/**
 * 消息投递器，给业务方使用，内部只有一个方法，用来发送消息
 *
 * 若上下文没有事务，则消息落地之后立即投递；若存在事务，则消息投递分为2步走：消息先落地，事务执行完毕之后再确定是否投递，用到了事务扩展点：TransactionSynchronization，事务执行完毕之后会回调TransactionSynchronization接口中的afterCompletion方法，再这个方法中确定是否投递消息。
 */
@Component
public class MsgSender {
    @Autowired
    private MsgOrderService msgOrderService;
    @Autowired
    private MsgService msgService;

    /**
     * 发送消息
     */
    public void send(String msg, int ref_type, String ref_id) {
        MsgOrderModel msgOrderModel = this.msgOrderService.insert(ref_type, ref_id);
        Long msg_order_id = msgOrderModel.getId();
        //TransactionSynchronizationManager.isSynchronizationActive可以用来判断事务同步是否开启了
        boolean isSynchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        /**
         * 若事务同步开启了，那么可以再事务同步中添加事务扩展点，则先插入消息，暂不发送，则再事务扩展点添加回调
         * 事务结束之后会自动回调扩展点TransactionSynchronizationAdapter的afterCompletion()方法
         * 咱们在这个方法中确定是否投递消息
         */
        if (isSynchronizationActive) {
            final Long msg_id = this.msgService.addMsg(msg, msg_order_id, false);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    //代码走到这里时，事务已经完成了（可能是回滚了或者是提交了）
                    //看一下消息关联的订单是否存在，如果存在，说明事务是成功的，业务是执行成功的，那么投递消息
                    if(msgOrderService.getById(msg_order_id) != null) {
                        System.out.println(String.format("准备投递消息，{msg_id: %s}", msg_id));
                        //事务成功：投递消息
                        msgService.confirmSendMsg(msg_id);
                    } else {
                        //事务回滚：取消投递消息
                        System.out.println(String.format("准备取消投递消息，{msg_id:%s}", msg_id));
                    }
                }
            });
        } else {
            //无事务的，直接插入并投递消息
            this.msgService.addMsg(msg, msg_order_id, true);
        }
    }
























}
