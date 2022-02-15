package us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Service3 {
    private IService s1;
    private IService s2;

    @Autowired
    @Qualifier("service1")
    public void setS1(IService s1) {
        this.s1 = s1;
    }
    //两个方法都有@Autowired注解，并且结合了@Qualifier注解来限定需要注入哪个bean

    @Autowired
    @Qualifier("service2")
    public void setS2(IService s2) {
        this.s2 = s2;
    }

    @Override
    public String toString() {
        return "Service3{" +
                "s1=" + s1 +
                ", s2=" + s2 +
                '}';
    }
}
