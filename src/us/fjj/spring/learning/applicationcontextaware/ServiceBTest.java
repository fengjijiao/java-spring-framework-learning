package us.fjj.spring.learning.applicationcontextaware;

import org.springframework.context.support.ClassPathXmlApplicationContext;

class ServiceBTest {
    /**
     * 在单例Bean中使用多例Bean:ApplicationContextAware接口实现，但是这样对spring的api有耦合的作用，
     * 可使用lookup-method的方式（在需要调用ServiceA的时候拦截该方法，去容器中取）解决这个问题
     * @param args
     */
    public static void main(String[] args) {
        /**
         * ApplicationContextAware接口实现
         */
        String path = "ApplicationContextAwareBean.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
        for (int i = 0; i < 3; i++) {
            ServiceB serviceB = context.getBean(ServiceB.class);
            System.out.println("ServiceB: "+serviceB);
            serviceB.say();
        }
        /**
         * 可以看出ServiceB是同一个对象，而ServiceA不是。
         *us.fjj.spring.learning.applicationcontextaware.ServiceA@39d9314d;
         * us.fjj.spring.learning.applicationcontextaware.ServiceA@b978d10;
         * us.fjj.spring.learning.applicationcontextaware.ServiceA@5b7a8434;
         */
        //去除配置文件中的 scope="prototype"后默认为单例模式，输出如下:
        /**
         * us.fjj.spring.learning.applicationcontextaware.ServiceA@10f7f7de;
         * us.fjj.spring.learning.applicationcontextaware.ServiceA@10f7f7de;
         * us.fjj.spring.learning.applicationcontextaware.ServiceA@10f7f7de;
         */
        context.close();

    }
}