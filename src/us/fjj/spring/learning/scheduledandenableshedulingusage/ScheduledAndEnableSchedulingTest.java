package us.fjj.spring.learning.scheduledandenableshedulingusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import us.fjj.spring.learning.scheduledandenableshedulingusage.test1.MainConfig1;
import us.fjj.spring.learning.scheduledandenableshedulingusage.test2.MainConfig2;
import us.fjj.spring.learning.scheduledandenableshedulingusage.test3.MainConfig3;

import java.util.concurrent.TimeUnit;

/**
 * @Scheduled & @EnableScheduling定时器详解
 *
 * spring中@Scheduled & @EnableScheduling这两个注解，可以用来快速开发定时器。
 */
public class ScheduledAndEnableSchedulingTest {
    /**
     * 如何使用?
     * 用法：
     * 1.需要定时执行的方法上加上@Scheduled注解，这个注解中可以指定定时执行的规则。
     * 2.Spring容器中使用@EnableScheduling开启定时任务的执行，此时spring容器才可以识别@Scheduled标注的方法，然后自动定时执行。
     *
     * 案例：
     * db中有很多需要推送的任务，然后将其检索出来，推送到手机端，来个定时器，每秒一次从库中检测需要推送的消息，然后推送到手机端。
     */
    @Test
    public void test1() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        //休眠一段时间，防止自动退出
        TimeUnit.SECONDS.sleep(3);
        /**
         * 模拟推送消息：1646789550907
         * 模拟推送消息：1646789551920
         * 模拟推送消息：1646789552916
         * 模拟推送消息：1646789553912
         */
    }

    /**
     * @Scheduled配置定时规则
     * @Scheduled可以用来配置定时器的执行规则，非常强大，@Scheduled中主要有8个参数，我们一一来了解一下。
     *
     *
     *
     * 1.cron
     * 该参数接收一个cron表达式，cron表达式是一个字符串，字符串以5或6个空格隔开，分开共6或7个域，每一个域代表一个含义。
     * cron表达式语法
     * [秒] [分] [时] [日] [月] [周] [年]
     * 其中年不是必须的域，可以省略
     * 序号   说明  必填  允许填写的值  允许的通配符
     * 1    秒   是   0-59    ,-*\/
     * 2    分   是   0-59    ,-*\/
     * 3    时   是   0-23    ,-*\/
     * 4    日   是   1-31    ,-*?/L W
     * 5    月   是   1-12/JAN-DEC    ,-*\/
     * 6    周   是   1-7 or SUN-SAT  ,-*?/L #
     * 7    年   否   1970-2099   ,-*\/
     * 通配符说明
     * *表示所有值。
     * ?表示不指定值。使用的场景为不需要关心当前设置这个字段的值。例如：要在每月的10号触发一个操作，但不关心是周几，所以[周]的位置的那个字段设置为?，具体设置为 0 0 0 10 * ?。
     * -表示区间。例如在[时]上设置10-12，则10,11,12点都会触发。
     * ,表示指定多个值，例如在[周]上设置MON,WED,FRI表示周一，周三，周五触发
     * /用于递增触发。如在[秒]上设置5/15表示从5s开始，每增15s触发一次（5,20,35,50）。在[日]上设置1/3所示每月1日开始，每隔3天触发一次。
     * L表示最后。如在[日]上设置，表示每月最后一天（依据当前月份，如果是2月还会依据是否是闰年[leap]）。在周字段上表示星期6，相当于7或SAT。如果在L前加上数字，则表示该数据的最后一个。例如在[周]上设置6L这样的格式，则表示本月的最后一个星期五。
     * W表示离指定日期的最近的工作日（周一到周五），例如在[日]字段上设置15W，表示离每月15日最近的工作日触发。如果15日正好是周六，则找最近的的周五（14日）触发，如果15日是周末，则找最近的下周一（16日）触发，如果15日正好是工作日（周一~周五）则在当天触发。如果指定格式为1W，它则表示每月1日往后最近的工作日触发。如果一日正好是周六，则在3号下周一触发，（注：W前只能设置具体的数组，不允许区间“-”）
     * #序号（表示每月的第几个周几），例如在[周]上设置6#3表示每月的第3个周六。注意如果指定#5，正好第五周没有周六，则不会触发该配置（用在母亲节或父亲节再适合不过了）；
     * 小提示：L和W可以一起组合使用。如果在[日]上设置LW，则表示在本月的最后一个工作日触发；[周]的设置，若使用英文字母是不区分大小写的，即MON和mon相同。
     *
     * 示例：
     * 每个5s执行一次 *\/5 * * * * ?
     * 每隔1分钟执行一次 0 *\/1 * * * ?
     * 每天23点执行一次 0 0 23 * * ?
     * 每天凌晨一点执行一次 0 0 1 * * ?
     * 每月1号凌晨一点执行一次 0 0 1 1 * ?
     * 每月最后一天23点执行一次 0 0 23 L * ?
     * 每周星期六凌晨一点执行一次 0 0 1 ? * SAT / 0 0 1 ? * L / 0 0 1 ? * 7
     * 在26分、29分、33分执行一次 0 26,29,33 * * * * ?
     * 每天的0点、13点、18点、21点都执行一次 0 0 0,13,18,21 * * * ?
     *
     * https://tool.lu/crontab/
     *
     * cron表达式使用占位符
     * 另外，cron属性接收的cron表达式支持占位符。
     * 如，配置文件：
     * time:
     *   cron: *\/5 * * * * *
     *   interval: 5
     * 每5s执行一次：
     * @Scheduled(cron="${time.cron}")
     * void testPlaceholder1() {
     *     System.out.println("Execute at " + System.currentTimeMillis());
     * }
     * @Scheduled(cron="*\/${time.interval} * * * * *")
     * void testPlaceholder2() {
     *     System.out.println("Execute at " + System.currentTimeMillis());
     * }
     *
     *
     *
     * 2.zone
     * 时区，接受一个java.util.TimeZone#ID。cron表达式会基于该时区解析。默认是一个空字符串，即取服务所在地的时区。比如我们一般使用的时区Asia/Shanghai。该字段我们一般留空。
     *
     *
     *
     * 3.fixedDelay
     * 上一次执行完毕时间点之后多长时间再执行。
     * 如：
     * @Scheduled(fixedDelay = 5000)//上一次执行完毕时间点之后5s再执行
     *
     *
     *
     * 4.fixedDelayString
     * 与3.fixedDelay意思相同，只是使用字符串形式。唯一不同的是支持占位符。
     * 如：
     * @Scheduled(fixedDelayString = "5000")//上一次执行完毕时间点之后5s再执行
     * 占位符的使用（配置文件中由配置： time.fixedDelay=5000）
     * @Scheduled(fixedDelayString = "${time.fixedDelay}")
     * void testFixedDelayString() {
     *     System.out.println("Execute at " + System.currentMillis());
     * }
     *
     *
     *
     * 5.fixedRate
     * 上一次开始执行时间点之后多长时间再执行。
     * 如：
     * @Schduled(fixedRate = 5000)//上一次开始执行时间点之后5s再执行
     *
     *
     *
     * 6.fixedRateString
     * 与fixedRate意思相同，只是使用字符串的形式，唯一不同的是支持占位符。
     *
     *
     *
     * 7.initialDelay
     * 第一次延迟多长时间后执行
     * 如：
     * @Scheduled(initialDelay=1000, fixedRate=5000)//第一次延迟1s后执行，之后按fixedRate的规则每5s执行一次
     *
     *
     *
     *
     * 8.initialDelayString
     * 与initialDelay意思相同，只是使用字符串的形式，唯一不同的是支持占位符。
     *
     */


    /**
     * @Schedules注解
     * 当一个方法上面需要同时指定多个定时规则的时候，可以通过这个来配置。
     * 如：
     * //2个定时器，500ms的，1000ms的
     * @Schedules({@Scheduled(fixedRate = 500), @Scheduled(fixedRate = 1000)})
     * public void push3() {}
     *
     *
     */

    /**
     * 为定时器定义线程池
     * 定时器默认情况下使用下面的线程池来执行定时任务的 new ScheduledThreadPoolExecutor(1)
     * 只有一个线程，相当于只有一个干活的人，如果需要定时执行的任务太多，这些任务只能排队执行，会出现什么问题？
     * 如果有些任务耗时比较长，导致其他任务排队时间比较长，不能有效的正常执行，直接影响到业务。
     *
     *
     * 看下面代码，2个方法，都使用了@Scheduled(fixedRate = 1000)，表示每秒执行一次，而push1方法中模拟耗时3s，方法会打印出线程名称、时间等信息，一会注意观察输出。
     *
     * 案例
     */
    @Test
    public void test2() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig2.class);
        context.refresh();
        TimeUnit.SECONDS.sleep(10);
        /**
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795445799
         * Thread[pool-2-thread-1,5,main] job1执行时间1646795448809
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795448810
         * Thread[pool-2-thread-1,5,main] job1执行时间1646795451818
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795451818
         * Thread[pool-2-thread-1,5,main] job1执行时间1646795454825
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795454826
         */
    }
    /**
     * 注意上面的输出，线程名称都是pool-2-thread-1，并且有个问题，push2中2次输出时间间隔3s，这就是由于线程池中只有一个线程导致了排队执行而产生的问题。
     * 可以通过之定义定时器中的线程池来解决这个问题，定义一个ScheduledExecutorService类型的bean，名称为taskScheduler。
     */
    @Test
    public void test3() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig3.class);
        context.refresh();
        TimeUnit.SECONDS.sleep(10);
        /**
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795758125
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795759129
         * Thread[pool-2-thread-3,5,main] job2执行时间1646795760124
         * Thread[pool-2-thread-2,5,main] job1执行时间1646795761135
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795761135
         * Thread[pool-2-thread-3,5,main] job2执行时间1646795762128
         * Thread[pool-2-thread-5,5,main] job2执行时间1646795763124
         * Thread[pool-2-thread-2,5,main] job2执行时间1646795764131
         * Thread[pool-2-thread-4,5,main] job1执行时间1646795764147
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795765127
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795766125
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795767124
         * Thread[pool-2-thread-4,5,main] job1执行时间1646795767154
         * Thread[pool-2-thread-1,5,main] job2执行时间1646795768124
         */
    }

    /**
     * 原理&源码
     * 在EnableScheduling中有个Import注解会引入一个bean（bean后置处理器），在b所有ean初始化完成之后，会自动调用这个bean中的postProcessAfterInitialization方法，会解析bean中所有包含@Scheduled方法，这些方法也就是需要实现定时器的方法。
     * 除此之外，ScheduledAnnotationBeanPostProcessor还实现了一个接口：SmartInitializingSingleton，SmartInitializingSingleton中有个方法afterSingletonsInstantiated会在spring容器中所有单例bean初始化完成之后调用，定时器的装配和启动都是在这个方法中进行的。
     * {@link ScheduledAnnotationBeanPostProcessor#afterSingletonsInstantiated()}
     */
}
