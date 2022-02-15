package us.fjj.spring.learning.scopeannotationusage;

/**
 * @Scope用来配置bean的作用域，等效于bean xml中的bean元素中的scope属性。
 */

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 常见2种用法
 * 1.和@Component一起使用在类上
 * 2.和@Bean一起标注在方法上
 */
public class ScopeAnnotationTest {
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        for (int i = 0; i < 3; i++) {
            System.out.println(context.getBean(Service1.class));
        }
        for (int i = 0; i < 3; i++) {
            System.out.println(context.getBean(Service3.class));
        }
        /**
         * us.fjj.spring.learning.scopeannotationusage.Service1@6e16b8b5
         * us.fjj.spring.learning.scopeannotationusage.Service1@43b4fe19
         * us.fjj.spring.learning.scopeannotationusage.Service1@25ddbbbb
         * us.fjj.spring.learning.scopeannotationusage.Service3@1536602f
         * us.fjj.spring.learning.scopeannotationusage.Service3@1536602f
         * us.fjj.spring.learning.scopeannotationusage.Service3@1536602f
         */
    }
}
