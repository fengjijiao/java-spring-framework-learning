package us.fjj.spring.learning.importannotationusage.test4;

public class Service2 {
    private Service1 service1;

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
