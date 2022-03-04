package us.fjj.spring.learning.aspectpointcutusage.test14;

public class BeanService {
    private String beanName;

    public BeanService(String beanName) {
        this.beanName = beanName;
    }

    public void m1() {
        System.out.println(this.beanName+".m1()");
    }
}
