package us.fjj.spring.learning.proxyclass;

public class ProxyClassTest {
    /**
     * 普通代理类Test
     * ServiceA和ServiceB均实现了IService接口，当需要计算IService的所有实现类方法的运行时间或添加执行前/后操作（可能需要频繁修改的功能）时，可能会产生较大的工作量，且在后期修改中会使code变得更加不利于维护。
     * 通常的做法是为IService接口创建一个代理类，通过代理类来间接访问IService接口的实现类，在这个代理类中去做耗时及发送至监控的代码。
     *
     * @param args
     */
    public static void main(String[] args) {
        IService serviceA = new ServiceProxy(new ServiceA());
        IService serviceB = new ServiceProxy(new ServiceB());
        serviceA.m1();
        serviceA.m2();
        serviceA.m3();
        serviceB.m1();
        serviceB.m2();
        serviceB.m3();
        /**
         * m1方法执行前的操作!
         * 我是ServiceA的m1方法！
         * m1方法执行后的操作！
         * 执行耗时227900ns
         * m2方法执行前的操作!
         * 我是ServiceA的m2方法！
         * m2方法执行后的操作！
         * 执行耗时124500ns
         * m3方法执行前的操作!
         * 我是ServiceA的m3方法！
         * m3方法执行后的操作！
         * 执行耗时81000ns
         * m1方法执行前的操作!
         * 我是ServiceB的m1方法！
         * m1方法执行后的操作！
         * 执行耗时129400ns
         * m2方法执行前的操作!
         * 我是ServiceB的m2方法！
         * m2方法执行后的操作！
         * 执行耗时74400ns
         * m3方法执行前的操作!
         * 我是ServiceB的m3方法！
         * m3方法执行后的操作！
         * 执行耗时68700ns
         */
    }
}
