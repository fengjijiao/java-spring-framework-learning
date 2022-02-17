package us.fjj.spring.learning.propertysourceandvalueannotationusage.test4;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DBUtil {
    /**
     * 返回邮件配置信息(值随机)
     * @return
     */
    public static Map<String, Object> getMailInfoFromDB() {
        Map<String, Object> map = new HashMap<>();
        map.put("mail.username", UUID.randomUUID().toString());
        map.put("mail.password", UUID.randomUUID().toString());
        return map;
    }
}
