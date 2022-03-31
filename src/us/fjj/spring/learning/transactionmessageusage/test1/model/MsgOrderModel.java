package us.fjj.spring.learning.transactionmessageusage.test1.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MsgOrderModel {
    private Long id;
    /**
     * 关联业务类型
     */
    private Integer ref_type;
    /**
     * 关联业务Id(ref_type & ref_id 唯一)
     */
    private String ref_id;
}
