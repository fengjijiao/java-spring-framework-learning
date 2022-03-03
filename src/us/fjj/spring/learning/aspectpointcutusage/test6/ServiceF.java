package us.fjj.spring.learning.aspectpointcutusage.test6;

public class ServiceF {
    public void m1(Object obj) {
        System.out.println(String.format("传入的参数类型是："+obj.getClass()+"，值为："+obj));
    }
}
