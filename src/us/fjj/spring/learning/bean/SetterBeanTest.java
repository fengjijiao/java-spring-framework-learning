package us.fjj.spring.learning.bean;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SetterBeanTest {

    public static void main(String[] args) {
        String path = "SetterBean.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
        SetterBean.IService service = context.getBean(SetterBean.IService.class);
        System.out.println(service);
        context.close();
    }
}
