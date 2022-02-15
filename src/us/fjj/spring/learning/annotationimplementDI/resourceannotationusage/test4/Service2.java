package us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test4;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Service2 {
    private Service1 service1;

    /**
     * @Resource所标注的方法只能有一个参数，否则会产生org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'service2' defined in file
     * 错误
     * @param service1
     */
    @Resource
    public void setService1(Service1 service1) {
        this.service1 = service1;
    }

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }
}
