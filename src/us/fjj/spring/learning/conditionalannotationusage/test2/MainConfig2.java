package us.fjj.spring.learning.conditionalannotationusage.test2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 阻止bean的注册
 */

@Configuration
public class MainConfig2 {
    @Conditional(MyCondition2.class)//使用了自定义条件类
    @Bean
    public String name() {//通过@Bean标注这个name，如果配置类成功解析，会将name方法的返回值作为bean注册到spring容器。
        return "LAOKAI";
    }

    @Bean
    public String address() {
        return "LK SB";
    }
}
