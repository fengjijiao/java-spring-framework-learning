package us.fjj.spring.learning.transactionusage.test3;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class User {
    private String username;
    private String password;
}
