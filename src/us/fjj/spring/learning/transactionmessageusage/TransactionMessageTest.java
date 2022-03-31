package us.fjj.spring.learning.transactionmessageusage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import us.fjj.spring.learning.transactionmessageusage.test1.MainConfig1;
import us.fjj.spring.learning.transactionmessageusage.test1.service.UserService;
import us.fjj.spring.learning.transactionmessageusage.test1.service.UserService1;

/**
 * 手把手带你实现事务消息
 * 目的：
 * 1.讨论一下消息投递的5种方式
 * 2.带你手写代码，实现事务消息的投递
 * 消息投递的5种方式
 * 2.1业务场景
 * 电商中有这样的一个场景：商品下单之后，需要给用户送积分，订单表和积分表分别在不同的db中，涉及到分布式事务的问题。
 * 我们通过可靠消息来解决这个问题：
 * 1.商品下单成功之后送积分的操作，我们使用mq来实现。
 * 2.商品下单成功之后，投递一条消息到mq，积分系统消费消息，给用户增加积分
 * 我们主要讨论一下，商品下单及投递消息到mq的操作，如何实现？每种方式优缺点？
 * <p>
 * 2.2方式一
 * 过程
 * step1: 开启本地事务
 * step2: 生成购物订单
 * step3: 投递消息到mq
 * step4: 提交本地事务
 * 这种方式是将发送消息放在了事务提交之前
 * 可能存在的问题
 * step3发生异常：导致step4失败，商品下单失败直接影响到商品下单业务
 * step4发生异常，其他step成功：商品下单失败，消息投递成功，给用户增加了积分
 * 2.3方式二：下面我们换种方式，我们将发送消息放到事务之后进行。
 * 过程
 * step1: 开启本地事务
 * step2: 生成购物订单
 * step3: 提交本地事务
 * step4: 投递消息到mq
 * 可能出现的问题
 * step4发送异常，其他step成功：导致商品下单成功，投递消息失败，用户未增加积分
 * <p>
 * 上面2种是比较常见的做法，也是最容易出错的。
 * <p>
 * 2.4方式三
 * step1: 开启本地事务
 * step2: 生成购物订单
 * step3: 本地库中插入一条需要发送消息的记录t_msg_record
 * step4: 提交本地事务
 * step5: 新增一个定时器，轮询t_msg_record，将待发送的记录投递到mq中
 * <p>
 * 这种方式借助了数据库的事务，业务和消息记录作为一个原子操作，业务成功之后，消息日志必定是存在的。解决了前2种方式遇到的问题。如果我们的业务系统比较单一，可以采用这种方式。
 * 对于微服务化的情况，上面这种方式不是太好，每个服务都需要上面的操作；也不利于扩展。
 * <p>
 * 2.5方式四
 * 增加一个消息服务及消息库，负责消息的落库、将消息发送投递到mq。
 * step1: 开启本地事务
 * step2: 生成购物订单
 * step3: 当前事务库中插入一条日志：生成一个唯一的业务id（msg_order_id），将msg_order_id和订单关联起来保存到当前事务所在的库中，
 * step4: 调用消息服务：携带msg_order_id，将消息先落地入库，此时消息的状态为待发送状态，返回消息id(msg_id)
 * step5: 提交本地事务
 * step6: 如果上面都成功了，调用消息服务，将消息投递到mq中；如果上面有失败的情况，则调用消息服务取消消息的发送。
 * 能想到上面这种方式，已经算是有很大的进步了，我们继续分析一下可能存在的问题：
 * 1.系统中增加了一个消息服务，商品下单操作依赖于该服务，业务对该服务依赖性比较高，当消息服务不可用时，整个业务将不可用。
 * 2.若step6失败，消息将处于待发送状态，此时业务方需要提供一个回查接口（通过msg_order_id查询），沿着业务是否执行成功；消息服务需新增一个定时任务，对于状态为待发送状态的消息做补偿处理，检查一下业务是否处理成功；从而确定消息是否投递还是取消发送。
 * 3.step4依赖于消息服务，如果消息服务性能不佳，会导致当前业务的事务提交时间延长，容易产生死锁，并导致并发性能降低。我们通常是比较忌讳在事务中做远程调用处理的，远程调用的性能和时间往往不可控，会导致当前事务变成一个大事务，从而引发其他故障。
 * <p>
 * <p>
 * 2.6方式五
 * 在以上方式中，我们继续改进，进而出现了更好的一种方式：
 * step1: 生成一个全局唯一业务消息id(bus_msg_id)，调用消息服务，携带bus_msg_id，将消息先落地入库，此时消息的状态为待发送状态，返回消息id（msg_id）
 * step2: 开启本地事务
 * step3: 生成购物订单
 * step4: 当前事务库插入一条日志（将step3中的业务和bus_msg_id关联起来）
 * step5: 提交本地事务
 * step6: 分两种情况：如果上面都成功，调用消息服务，将消息投递到mq中；如果上面有失败的情况，则调用消息服务取消消息的发送。
 * 若step6失败，消息将处于待发送状态，此时业务方需要提供一个回查接口（通过bus_msg_id查询），验证业务是否执行成功;
 * 消息服务需新增一个定时任务，对于状态为待发送的消息做补偿处理，检查一下业务是否处理成功；从而确定消息是投递还是取消发送。
 * <p>
 * 方式五和方式四对比，比较好的一个地方：将调用消息服务，消息落地操作，放在了事务之外进行，这点小的改进其实算是一个非常好的优化，减少了本地事务的执行时间，从而可以提升并发量，阿里有个消息中间件RocketMQ就支持方式五这种，大家可以去用用。
 */
public class TransactionMessageTest {
    private AnnotationConfigApplicationContext context;
    private UserService userService;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void before() {
        this.context = new AnnotationConfigApplicationContext(MainConfig1.class);
        userService = context.getBean(UserService.class);
        this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        jdbcTemplate.update("truncate table t_user");
        jdbcTemplate.update("truncate table t_msg");
        jdbcTemplate.update("truncate table t_msg_order");
    }

    @Test
    public void test() throws JSONException {
        this.userService.register(1L, "java");
        /**
         * 用户注册: [user_id:1,user_name:java]
         * 插入消息：MsgModel(id=1, msg=[user_id:1,user_name:java], msg_order_id=1, status=0)
         * 准备投递消息，{msg_id: 1}
         */
    }


    @Test
    public void test2() throws JSONException {
        this.userService.registerFail(1L, "java");
        /**
         * 用户注册: [user_id:1,user_name:java]
         * 插入消息：MsgModel(id=1, msg=[user_id:1,user_name:java], msg_order_id=1, status=0)
         * 准备取消投递消息，{msg_id:1}
         *
         * java.lang.RuntimeException: 故意失败！
         */
    }


    /**
     * 嵌套事务
     * 事务发送是跟随当前所在的事务的，当事务提交了，消息一定会被投递出去，当前事务回滚，消息会被取消投递。
     */
    @Test
    public void test3() {
        UserService1 userService1 = this.context.getBean(UserService1.class);
        userService1.nested();
        /**
         * 插入消息：MsgModel(id=1, msg=消息1, msg_order_id=1, status=0)
         * 用户注册: [user_id:1,user_name:张三]
         * 插入消息：MsgModel(id=2, msg=[user_id:1,user_name:张三], msg_order_id=2, status=0)
         * 准备投递消息，{msg_id: 2}
         * 用户注册: [user_id:2,user_name:李四]
         * 插入消息：MsgModel(id=3, msg=[user_id:2,user_name:李四], msg_order_id=3, status=0)
         * 准备取消投递消息，{msg_id:1}
         * 准备取消投递消息，{msg_id:3}
         *
         * java.lang.RuntimeException: 故意失败！
         */
    }

    /**
     * 小结
     *
     * 事务消息分2步走，先落库，此时消息带投递，等到事务执行完毕之后，再确定是否投递，用到的关键技术点是事务扩展接口：TransactionSynchronization，事务执行完毕之后会自动回调接口中的afterCompletion方法。
     *
     * 遗留的一个问题：消息补偿操作
     * 当事务消息刚落地，此时处于待投递状态，系统刚好down机了，此时系统恢复之后，需要有个定时器来处理这种消息，拿着消息中的msg_order_id去业务库查一下订单是否存在，如果存在，则投递消息，否则取消投递。
     *
     */

    /**
     * 总结
     * 需要掌握的重点：
     * 1.消息投递的5种方式的推演，要熟练掌握其优缺点
     * 2.方式4中事务消息的代码实现
     *
     * 消息服务使用频率很高，通常作为系统中的基础服务使用，大家可以尝试一下开发一个独立的消息服务，提供给其他服务使用。
     */

}
