package us.fjj.spring.learning.importannotationusage.test2;

import org.springframework.context.annotation.Import;
import us.fjj.spring.learning.importannotationusage.test2.module1.ConfigModule1;
import us.fjj.spring.learning.importannotationusage.test2.module2.ConfigModule2;

/**
 * 通过Import来汇总多个@Configuration标注的配置类
 */
@Import({
        ConfigModule1.class,
        ConfigModule2.class
})
public class MainConfig2 {
}
