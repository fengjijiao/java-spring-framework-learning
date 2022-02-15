package us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Service3 {
    @Autowired
    @Qualifier("service2")
    private IService service;//@1

    /**
     * 这里的限定符值为service2，容器中IService类型的bean有2个【service1和service2】，当类上没有标注@Qualifier注解的时候，可以理解为：
     * bean的名称就是限定符的值，所以@1这里会匹配到service2
     */

    @Override
    public String toString() {
        return "Service3{" +
                "service=" + service +
                '}';
    }
}
