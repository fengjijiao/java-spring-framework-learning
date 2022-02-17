package us.fjj.spring.learning.propertysourceandvalueannotationusage.test4;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

public class RefreshConfigUtil {
    /**
     * 模拟改变数据库配置信息
     */
    public static void updateDBConfig(AbstractApplicationContext context) {
        //更新context中的mailPropertySource配置信息
        refreshMailPropertySource(context);
        //清空BeanRefreshScope中所有bean的缓存
        BeanRefreshScope.clean();
    }

    public static void refreshMailPropertySource(AbstractApplicationContext context) {
        //获取一份新的配置信息
        Map<String, Object> mailInfoFromDB = DBUtil.getMailInfoFromDB();
        //将其丢在MapPropertySource中（MapPropertySource类是spring提供的一个类，是PropertySource的子类）
        MapPropertySource mailPropertySource = new MapPropertySource("mail", mailInfoFromDB);
        context.getEnvironment().getPropertySources().addFirst(mailPropertySource);
    }
}
