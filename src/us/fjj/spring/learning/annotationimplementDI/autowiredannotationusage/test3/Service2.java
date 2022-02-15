package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    private Service1 service1;

    /**
     * 在方法上标注了@Autowired，Spring容器会调用这个方法，从容器中查找Service1类型的bean，然后注入。
     * @param service1
     */
    @Autowired
    public void injectService1(Service1 service1) {
        System.out.println("执行了Service2的injectService1方法");
        this.service1 = service1;
    }

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }
}
