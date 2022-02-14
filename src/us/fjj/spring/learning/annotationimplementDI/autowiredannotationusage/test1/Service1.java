package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1;

public class Service1 implements IService {
    @Override
    public void m1() {
        System.out.println("这里是Service1的m1方法！");
    }
}
