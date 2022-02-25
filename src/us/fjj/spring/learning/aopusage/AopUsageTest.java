package us.fjj.spring.learning.aopusage;

/**
 * Aop详解
 * 通俗的理解：在程序中具有公共特性的某些类/某些方法上进行拦截，在方法执行的前面/后面/执行结果返回后增加执行一些方法。
 *
 * Spring中AOP的一些概念
 * 目标对象(target): 目标对象指将要被增强的对象，即包含主业务逻辑的类对象。
 * 连接点(JoinPoint): 程序运行的某一个点，比如执行某个方法，在Spring AOP中Join Point总是表示一个方法的执行.
 * 代理对象（Proxy）: AOP中会通过代理的方式，对目标对象生成一个代理对象，代理对象中会加入需要增强的功能，通过代理对象来间接访问目标对象，起到增强目标对象的效果。
 * 通知(Advice)：需要在目标对象中增强的功能，如果上面说的：业务方法前验证用户的功能、方法执行之后打印方法的执行日志。
 * 通知中有两个重要的信息：方法的什么地方，执行什么操作，这2个信息通过通知来指定。
 * 方法的什么地方？之前、之后、包裹目标方法、方法抛出异常后等。
 * 如：
 * 在方法执行之前验证用户是否有效。
 * 在方法执行之后，打印方法的执行耗时。
 * 在方法抛出异常后，记录异常信息发送到mq。
 * 切入点（Pointcut）：用来指定需要将通知使用到哪些地方，比如需要用在哪些类的哪些方法上，切入点就是做这个配置的。
 * 切面（Aspect）：通知（Advice）和切入点（Pointcut）的组合。切面来定义在哪些地方（Pointcut）执行什么操作（Advice）。
 * 顾问（Advisor）：Advisor其实它就是Pointcut与Advice的组合，Advice是要增强的逻辑，而增强的逻辑要在什么地方执行是通过Pointcut来指定的，所以Advice必须与Pointcut组合在一起，这就诞生了Advisor这个类， spring Aop中提供了一个Advisor接口即将Pointcut和Advice组合起来。
 * Advisor有好几个称呼：顾问、通知器。
 * JoinPoint、Advice、Pointcut、Advisor在spring中都定义了接口和类来表示这些对象。
 */
public class AopUsageTest {
}
