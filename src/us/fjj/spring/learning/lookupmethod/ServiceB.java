package us.fjj.spring.learning.lookupmethod;

public class ServiceB {
    public void say() {
        ServiceA serviceA = this.getServiceA();
        System.out.println("ServiceA:"+serviceA);
    }

    /**
     * 注意这里返回null，在配置文件中对该方法进行拦截
     * @return
     */
    public ServiceA getServiceA() {
        return null;
    }
}
