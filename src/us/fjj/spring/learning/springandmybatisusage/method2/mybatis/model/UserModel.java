package us.fjj.spring.learning.springandmybatisusage.method2.mybatis.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserModel {
    private Long id;
    private String name;
}
