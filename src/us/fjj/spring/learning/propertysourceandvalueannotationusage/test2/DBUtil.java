package us.fjj.spring.learning.propertysourceandvalueannotationusage.test2;

import java.util.HashMap;
import java.util.Map;

public class DBUtil {
    public static Map<String, Object> getMailInfoFromDB() {
        Map<String, Object> map = new HashMap<>();
        map.put("mail.host", "222.22.22.22");
        map.put("mail.username", "root");
        map.put("mail.password", "default");
        return map;
    }
}
