package us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Service3 {
    private IService s1;
    private IService s2;

    @Autowired
    public void injectMethod(@Qualifier("service1") IService s1, @Qualifier("service2") IService s2) {
        this.s1 = s1;
        this.s2 = s2;
    }
    //方法上标注了@Autowired注解，说明会被注入依赖，2个参数上分别使用了限定符来指定具体需要注入哪个bean

    @Override
    public String toString() {
        return "Service3{" +
                "s1=" + s1 +
                ", s2=" + s2 +
                '}';
    }
}
