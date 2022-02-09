package us.fjj.spring.learning.bean;

public class SetterBean {
    public interface IService {
    }

    public static class ServiceA implements IService {
    }

    public static class ServiceB implements IService {
    }

    private IService service;

    public void setService(IService service) {
        this.service = service;
    }
    //    private List<IService> service;
//
//    public void setService(List<IService> service) {
//        this.service = service;
//    }

    @Override
    public String toString() {
        return "SetterBean{" +
                "service=" + service +
                '}';
    }
}
