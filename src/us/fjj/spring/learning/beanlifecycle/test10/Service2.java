package us.fjj.spring.learning.beanlifecycle.test10;

import org.springframework.beans.factory.annotation.Autowired;

public class Service2 {
    @Autowired
    private Service1 service1;//标注了@Autowired，说明需要注入这个对象

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }
}
