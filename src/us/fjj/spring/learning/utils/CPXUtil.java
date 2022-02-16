package us.fjj.spring.learning.utils;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CPXUtil {
    public static void printAllBean(String path) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
    }
}
