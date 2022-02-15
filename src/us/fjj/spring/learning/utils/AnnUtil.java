package us.fjj.spring.learning.utils;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnUtil {
    public static void printAllBean(Class<?> cls) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(cls);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
    }
}
