package us.fjj.spring.learning.proxyclass;

import org.springframework.util.StopWatch;

public class ServiceProxy implements IService {
    private IService service;

    public ServiceProxy(IService service) {
        this.service = service;
    }

    @Override
    public void m1() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("m1方法执行前的操作!");
        this.service.m1();
        System.out.println("m1方法执行后的操作！");
        stopWatch.stop();
        System.out.println("执行"+stopWatch.getLastTaskName()+"耗时"+stopWatch.getLastTaskTimeNanos()+"ns");
    }

    @Override
    public void m2() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("m2方法执行前的操作!");
        this.service.m2();
        System.out.println("m2方法执行后的操作！");
        stopWatch.stop();
        System.out.println("执行"+stopWatch.getLastTaskName()+"耗时"+stopWatch.getLastTaskTimeNanos()+"ns");
    }

    @Override
    public void m3() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("m3方法执行前的操作!");
        this.service.m3();
        System.out.println("m3方法执行后的操作！");
        stopWatch.stop();
        System.out.println("执行"+stopWatch.getLastTaskName()+"耗时"+stopWatch.getLastTaskTimeNanos()+"ns");
    }
}
