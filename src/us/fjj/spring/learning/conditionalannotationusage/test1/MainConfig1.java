package us.fjj.spring.learning.conditionalannotationusage.test1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 阻止配置类的处理
 */
@Conditional(MyCondition1.class)//使用了自定义条件类
@Configuration
public class MainConfig1 {
    @Bean
    public String name() {//通过@Bean标注这个name，如果配置类成功解析，会将name方法的返回值作为bean注册到spring容器。
        return "LAOKAI";
    }
}
