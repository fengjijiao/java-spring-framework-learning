package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1;

import javax.annotation.Priority;

@Priority(1)
public class Service2 implements IService {
    @Override
    public void m1() {
        System.out.println("这里是Service2的m1方法！");
    }
}
