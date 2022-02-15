package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    private Service1 service1;

    @Autowired
    public void injectTest(Service1 service1, @Autowired(required = false) String name) {//在方法产生上添加@Autowired(required=false)表示：若找不到String类型的bean来注入，则注入null
        System.out.println("service1="+service1+",name="+name);
        this.service1 = service1;
    }

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }
}
