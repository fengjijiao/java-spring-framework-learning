package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1;

import org.springframework.beans.factory.annotation.Autowired;

public class MainRun {
    //method1 start
    @Autowired
    private IService service1;
    //method1 end

    //method2 start
    //在Spring 4.0以后，如果只有一个带参数的构造函数，那么spring会自动填充参数注入（可不写@Autowired）
//    MainRun(IService service1) {
//        this.service1 = service1;
//    }
    //method2 stop
    //method3 start
//    @Autowired
//    public void setService1(IService service1) {
//        this.service1 = service1;
//    }
    //method3 stop

    public void m1() {
        System.out.println("这里是MainRun的m1方法!");
        service1.m1();
    }
}
