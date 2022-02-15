package us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test2;

public class Service3 {
    private IService service;

    public Service3(IService service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "Service3{" +
                "service=" + service +
                '}';
    }
}
