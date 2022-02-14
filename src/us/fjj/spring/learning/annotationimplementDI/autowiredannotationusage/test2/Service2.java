package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test2;

import org.springframework.stereotype.Component;

@Component
public class Service2 {
    private Service1 service1;

    public Service2(Service1 service1) {
        this.service1 = service1;
    }

    /**
     * 通过构造函数注入时，同时存在无参构造函数和有参构造函数（且有参构造函数上没有@Autowired注解），将会优先执行无参构造函数，而不会注入Service1
     * 若一方存在Autowired注解则使用该构造函数完成依赖注入
     */

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }
}
