package us.fjj.spring.learning.transactionmessageusage.test1.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MsgModel {
    private Long id;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 消息订单Id
     */
    private Long msg_order_id;
    /**
     * 消息状态，0：待投递，1：已发送，2：取消发送
     */
    private Integer status;























}
