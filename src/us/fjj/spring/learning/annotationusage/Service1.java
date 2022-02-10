package us.fjj.spring.learning.annotationusage;

@ParamsAnnotation(param3 = true, param4 = {"o", "k"})
public class Service1 {
    public void m1() {
        System.out.println("这里是Service1的m1方法！");
    }
}
