package us.fjj.spring.learning.cacheusage;

/**
 * 缓存使用（@EnableCaching、@Cacheable、@CachePut、@CacheEvict、@Caching、@CacheConfig）
 * 主要讲解spring中缓存的使用
 */

import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import us.fjj.spring.learning.cacheusage.test2.ArticleService;
import us.fjj.spring.learning.cacheusage.test2.MainConfig2;
import us.fjj.spring.learning.cacheusage.test3.ArticleService2;
import us.fjj.spring.learning.cacheusage.test3.MainConfig3;
import us.fjj.spring.learning.cacheusage.test4.ArticleService4;
import us.fjj.spring.learning.cacheusage.test4.MainConfig4;
import us.fjj.spring.learning.cacheusage.test5.ArticleService5;
import us.fjj.spring.learning.cacheusage.test5.MainConfig5;
import us.fjj.spring.learning.cacheusage.test6.ArticleService6;
import us.fjj.spring.learning.cacheusage.test6.MainConfig6;
import us.fjj.spring.learning.cacheusage.test7.ArticleService7;
import us.fjj.spring.learning.cacheusage.test7.MainConfig7;
import us.fjj.spring.learning.cacheusage.test8.ArticleService8;

import java.util.*;

/**
 * 背景
 * 缓存大家都有了解过吧，主要用来提升系统查询速度。
 * 比如电商中商品详细信息，这些信息通常不会经常变动但是会高频访问，我们可以将这些信息从db中拿出来放到缓存中（比如redis中、本地内存中），当获取的时候，先从缓存中获取，缓存中没有的时候，再从db中获取，然后将其再丢入缓存中，当商品信息被变更之后，可以将缓存中的信息剔除或者将最新的数据丢到缓存中。
 *
 * Spring中提供了一整套的缓存解决方案，使用起来特别容易，主要通过注解的方式使用缓存，常用的有5个注解。
 * 本文中会大量用到SpEL表达式。
 */
public class CacheTest {
    /**
     * @EnableCaching: 启用缓存功能
     * 开启缓存功能，配置类中需要加上这个注解，有了这个注解之后，spring才知道你需要使用缓存功能，其他和缓存相关的注解才会有效，spring中主要是通过aop实现的，通过aop来拦截需要使用缓存的方法，实现缓存的功能。
     */
    /**
     * @Cacheable: 赋予缓存功能
     * 作用：
     * @Cacheable可以标记在一个方法上，也可以标记在一个类上，当标记在一个方法上时表示该方法是支持缓存的，当标记在一个类上时则表示该类所有的方法都是支持缓存的。对于一个支持缓存的方法，Spring会在其被调用后将其返回值缓存起来，以保证下次利用相同的参数来执行该方法时可以直接从缓存中获取结果，而不需要在此执行该方法。Spring在缓存方法的返回值时是以键值对进行缓存的，值就是方法的返回结果，至于键的话，Spring又支持两种策略，默认策略和自定义策略。
     * 需要注意的是当一个支持缓存的方法在对象内部被调用时不会触发缓存功能的，@Cacheable可以指定3个属性，value,key和condition。
     *
     * value属性：指定Cache名称
     * value和cacheNames属性作用一样，必须指定其中一个，表示当前方法的返回值是会被缓存在哪个Cache上的，对应Cache的名称，其可以是一个Cache也可以是多个Cache，每个Cache有一个名字，你需要将方法的返回值放到哪个缓存中，需要通过缓存的名称来指定。
     */
    /**
     * 案例1
     * 下面list方法加上了缓存的功能，将其结果放在缓存cache1中。
     */
//    @Component
//    public static class ArticleService {
//        @Cacheable(cacheNames = {"cache1"})
//        public List<String> list() {
//            System.out.println("获取文章列表");
//            return Arrays.asList("spring", "mysql", "java高并发", "mybatis", "maven");
//        }
//    }

    /**
     * 下面来个配置类MainConfig1，必须加上@EnableCaching注解用来启用缓存功能。
     * 然后在配置类中需要定义一个Bean: 缓存管理器，类型为CacheManager，CacheManager这个是个接口，有好几个实现（比如redis、ConcurrentMap来存储信息），此处我们使用ConcurrentMapCacheManager，内部使用ConcurrentHashMap将缓存信息直接存储在本地jvm内存中，不过线上环境一般是集群的方式，可以通过redis实现。
     */
//    @EnableCaching
//    @ComponentScan
//    @Configuration
//    public static class MainConfig2 {
//        //@1: 缓存管理器
//        @Bean
//        public CacheManager cacheManager() {
//            //创建缓存管理器（ConcurrentMapCacheManager）:其内部使用ConcurrentMap实现的，构造器用来指定缓存的名称，可以指定多个。
//            ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager("cache1");
//            return cacheManager;
//        }
//    }

    @Test
    public void test24() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig2.class);
        context.refresh();
        ArticleService articleService = context.getBean(ArticleService.class);
        System.out.println(articleService.list());
        System.out.println(articleService.list());
        System.out.println(articleService.list());
        /**
         * 获取文章列表
         * [spring, mysql, java高并发, mybatis, maven]
         * [spring, mysql, java高并发, mybatis, maven]
         * [spring, mysql, java高并发, mybatis, maven]
         */
        /**
         * 从第一行可以看出，第一次进入到list方法内部了，第二次没有进入list方法内部，而是从缓存中获取的.
         */
    }

    /**
     * key属性：自定义key
     * key属性用来指定Spring缓存方法的返回结果时对应的key的，上面说了你可以将Cache理解为一个hashMap，缓存以key->value的形式存储在hashMap中，value就是需要缓存值（即方法的返回值）。
     *
     * key属性支持SpEL表达式；当我们没有指定该属性时，Spring将使用默认策略生成key(org.springframework.cache.interceptor.SimpleKeyGenerator)，默认会以方法参数创建key.
     *
     * 自定义策略是指我们可以通过SpEL表达式来指定我们的key，这里的SpEL表达式可以使用方法参数及他们对应的属性，使用方法参数时我们可以直接使用“#参数名”或者"#p参数index"。
     *
     * Spring还为我们提供了一个root对象可以用来生成key，通过该root对象我们可以获取到以下信息。
     * 属性名称         描述          示例
     * methodName       当前方法名        #root.methodName
     * method           当前方法            #root.method.name
     * target           当前被调用的对象        #root.target
     * targetClass      当前被调用的对象的class  #root.targetClass
     * args             当前方法参数组成的数组     #root.args[0]
     * caches           当前被调用的方法使用的Cache    #root.caches[0].name
     *
     * 这里我们主要看一下自定义策略。
     */
    @Test
    public void test25() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig3.class);
        context.refresh();
        ArticleService2 articleService2 = context.getBean(ArticleService2.class);

        //page=1,pageSize=10调用2次
        System.out.println(articleService2.getPage(1, 10));
        System.out.println(articleService2.getPage(1, 10));

        //page=2,pageSize=10调用2次
        System.out.println(articleService2.getPage(2, 10));
        System.out.println(articleService2.getPage(2, 10));

        {
            System.out.println("下面打印出cache1缓存中的key列表");
            ConcurrentMapCacheManager cacheManager = context.getBean(ConcurrentMapCacheManager.class);
            ConcurrentMapCache cache1 = (ConcurrentMapCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }
        /**
         * 从db中获取数据：page-1-pageSize-10
         * page-1-pageSize-10
         * page-1-pageSize-10
         * 从db中获取数据：page-2-pageSize-10
         * page-2-pageSize-10
         * page-2-pageSize-10
         * 下面打印出cache1缓存中的key列表
         * us.fjj.spring.learning.cacheusage.test3.ArticleService2-2-10
         * us.fjj.spring.learning.cacheusage.test3.ArticleService2-1-10
         */
    }

    /**
     * condition属性：控制缓存的使用条件
     * 有时候，可能我们希望查询不走缓存，同时返回的结果也不要被缓存，那么就可以通过condition属性来实现，condition属性默认为空，表示将缓存所有的调用情形，其值是通过SpEL表达式来指定的，当为true时表示先尝试从缓存中获取；若缓存中不存在，则执行方法，并将方法返回值丢到缓存中；当为false时，不走缓存、直接执行方法，并且返回结果也不会丢到缓存中。
     * 其值SpEL的写法和key属性类似.
     *
     * 案例3
     * ArticleService添加下面的代码，方法的第二个参数cache用来控制是否走缓存，将condition的值指定为#cache。
     */
    @Test
    public void test27() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig4.class);
        context.refresh();

        ArticleService4 articleService4 = context.getBean(ArticleService4.class);
        System.out.println(articleService4.getLxh("sb", true));
        System.out.println(articleService4.getLxh("sb", true));

        System.out.println(articleService4.getLxh("sb2", false));
        System.out.println(articleService4.getLxh("sb2", false));
        /**
         * 开始执行getLxh
         * [sb, lxh, lxh!]
         * [sb, lxh, lxh!]
         * 开始执行getLxh
         * [sb2, lxh, lxh!]
         * 开始执行getLxh
         * [sb2, lxh, lxh!]
         */
    }

    /**
     * unless属性：控制是否需要将结果丢到缓存中
     * 用于否决方法缓存的SpEL表达式。与condition不同，此表达式是在调用方法后计算的，因此可以引用结果。默认值为“”，这意味着缓存永远不会被否决。
     *
     * 前提是condition为空或者为true的情况下，unless才有效，condition为false时，unless无效，unless为true时，方法返回结果不会丢到缓存中；unless为false，方法返回结果将会丢到缓存中。
     *
     * 案例4
     * 当返回结果为null时，不要将结果进行缓存
     */
    @Test
    public void test28() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig5.class);
        context.refresh();

        ArticleService5 articleService5 = context.getBean(ArticleService5.class);

        System.out.println(articleService5.findById(1L));
        System.out.println(articleService5.findById(1L));
        System.out.println(articleService5.findById(3L));
        System.out.println(articleService5.findById(4L));
        {
            System.out.println("下面打印出缓存cache1中的key列表");
            ConcurrentMapCacheManager cacheManager = context.getBean(ConcurrentMapCacheManager.class);
            ConcurrentMapCache cache1 = (ConcurrentMapCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }
        /**
         * 获取文章: 1
         * hello world!
         * hello world!
         * 获取文章: 3
         * null
         * 获取文章: 4
         * null
         * 下面打印出缓存cache1中的key列表
         * findById1
         */
    }


    /**
     * condition和unless对比
     *
     * 缓存的使用过程中有2个点：
     * 1.查询缓存中是否有数据
     * 2.如果缓存中没有数据，则去执行目标方法，然后将方法结果丢到缓存中。
     *
     * spring中通过condition和unless对这两点进行干预。
     * condition作用于上面2个过程，当为true的时候，会尝试从缓存中获取数据，如果没有，会执行方法，然后将方法返回值丢到缓存中；如果为false，则直接调用目标方法，并且结果不会放在缓存中。
     *
     * 而unless在condition为true的情况下才有效，用来判断上面的第二点，是否不要将结果丢到缓存中，如果为true，则结果不会丢到缓存中，如果为false,则结果会丢到缓存中，并且unless中可以使用SpEL表达式通过#result获取方法返回值。
     *
     */








    /**
     * @CachePut：将结果放入缓存
     * 作用：
     * @CachePut也可以标注在类或者方法上，被标注的方法每次都会被调用，然后方法执行完毕之后，会将方法结果丢入缓存中；当标注在类上，相当于在类的所有方法上标注了@CachePut。
     *
     * 有3种情况，结果不会丢到缓存
     * 1.当方法向外抛出的时候
     * 2.condition的计算结果为false的时候
     * 3.unless的计算结果为true的时候
     *
     * 源码和Cacheable类似，包含的参数类似。
     *
     * value和cacheNames: 用来指定缓存名称，可以指定多个。
     * key: 缓存的key, SpEL表达式，写法参考@Cacheable中的key
     * condition: SpEL表达式，写法和@Cacheable中的condition一样，当为空或者结算结果为true的时候，方法的返回值才会丢到缓存中，否则结果不会丢到缓存中
     * unless: 当condition为空或者计算结果为true的时候，unless才会起效；true: 结果不会被丢到缓存中，false: 结果会被丢到缓存。
     *
     * 案例5
     * 实现新增文章操作，然后将文章丢到缓存中，注意下面的@CachePut的cacheNames、key2个参数和案例4中的findById方法上的@Cacheable中的一样，说明他们共用一个缓存，key也是一样的，那么当add方法执行完毕之后，再去调用findById方法，则可从缓存中直接取数据。
     *
     */
    @Test
    public void test29() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig6.class);
        ArticleService6 articleService6 = context.getBean(ArticleService6.class);

        //新增3篇文章，由于add方法上面有@CachePut注解，所以新增之后会自动丢到缓存中
        articleService6.add(1L, "java");
        articleService6.add(2L, "maven");
        articleService6.add(3L, "mysql");

        //然后调用findById获取，看看是否会走缓存
        System.out.println("调用findById方法，尝试从缓存中获取");
        System.out.println(articleService6.findById(1L));
        System.out.println(articleService6.findById(2L));
        System.out.println(articleService6.findById(3L));

        {
            System.out.println("下面打印出cache1缓存中的key列表");
            ConcurrentMapCacheManager cacheManager = context.getBean(ConcurrentMapCacheManager.class);
            ConcurrentMapCache cache1 = (ConcurrentMapCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }

        /**
         * 新增文章：1
         * 新增文章：2
         * 新增文章：3
         * 调用findById方法，尝试从缓存中获取
         * java
         * maven
         * mysql
         * 下面打印出cache1缓存中的key列表
         * findById3
         * findById2
         * findById1
         */
    }

    /**
     * @CacheEvict: 缓存清理
     * 作用：
     * 用来清理缓存的，@CacheEvict也可以标注在类或者方法上，被标注在方法上， 则目标方法被调用的时候，会清除指定的缓存；当标注在类上，相当于在类的所有方法上标注了@CacheEvict。
     * 参数：
     * value=cacheNames: cache的名称
     * key: 缓存的key，写法参考上面@Cacheable注解的key
     * condition: @CacheEvict注解生效的条件，值为spel表达式，写法参考@Cacheable注解中的condition
     * allEntries: 是否清理cacheNames指定的缓存中的所有缓存信息，默认是false；可以将一个cache想象为一个HashMap，当allEntries为true时，相当HashMap.clear()
     * beforeInvocation: 何时执行清除操作（方法执行前 or 方法执行成功之后）
     *                   true: @CacheEvict标注的方法执行之前，执行清除操作
     *                   false: @CacheEvict标注的方法执行成功之后，执行清除操作，当方法抛出异常的时候，不会执行清除操作。
     *
     *
     *
     * 会清除哪些缓存？
     * 默认情况下会清除cacheNames指定的缓存中key参数指定的缓存信息。
     * 但当allEntries为true的时候，会清除cacheNames指定的缓存中的所有缓存信息。
     *
     * 具体什么时候清除缓存？
     * 这个是通过beforeInvoation参数控制的，这个参数默认是false,默认会在目标方法成功执行之后执行清除操作，若方法向外抛出了异常，不会执行清理操作；
     * 如果beforeInvocation为true，则方法被执行之前就会执行缓存清理操作，方法执行之后不会再执行了。
     *
     * 案例6
     * ArticleService中新增一个方法，使用@CacheEvict标注，这个方法执行完毕之后，会清理cache1中key=findById+参数id的缓存信息，注意cacheNames和key两个参数的值和findById中这2个参数的值一样。
     *
     */
    @Test
    public void test30() {
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig7.class);
        ArticleService7 articleService7 = context.getBean(ArticleService7.class);

        //第一次调用findById，缓存中没有，则调用方法将结果丢到缓存中
        System.out.println(articleService7.findById(1L));
        //第二次调用findById，缓存中存在，直接从缓存中获取
        System.out.println(articleService7.findById(1L));

        //执行删除操作，delete方法上有@CacheEvict方法，会清除缓存
        articleService7.delete(1L);

        //再次调用findById,发现缓存中没有了，则会调用目标方法
        System.out.println(articleService7.findById(1L));
        /**
         * 获取文章: 1
         * hello world!
         * hello world!
         * 删除文章: 1
         * 获取文章: 1
         * hello world!
         */
    }

    /**
     * @Caching: 缓存注解组
     * 当在类上或者同一个方法上同时使用@Cacheable、@CachePut、@CacheEvict这几个注解中的多个的时候，此时可以使用@Caching这个注解来实现。
     */
    /**
     * @see org.springframework.cache.annotation.Caching
     *
     * 它包含参数
     * Cacheable[] cacheable();
     * CachePut[] put();
     * CacheEvict[] evict();
     */
    @Test
    public void test31() {
        //
    }


    /**
     * @CacheConfig: 提取公共配置
     * 这个注解标注在类上，可以将其他几个缓存注解（@Cacheable、@CachePut、@CacheEvict）的公共参数给提取出来放在@CacheConfig中。
     * 比如当一个类中有很多方法都需要使用（@Cacheable、@CachePut、@CacheEvict）这些缓存注解的时候，这3个注解的源码，他们有很多公共的属性，比如：cacheNames、keyGenerator、cacheManager、cacheResolver，若这些属性值都是一样的，可以将其提取出来，放在@CacheConfig中，不过这些注解（@Cacheable、@CachePut、@CacheEvict）中也可以指定属性的值对@CacheConfig中的属性进行覆盖
     */
    /**
     * @see ArticleService8
     */


    /**
     * 原理
     * spring中的缓存主要是利用spring中aop实现的，通过aop对需要使用缓存的bean创建代理对象，通过代理对象拦截目标方法的执行，实现缓存功能。
     * 重点在于@EnableCaching这个注解，可以从@Import这个注解看起
     * @see EnableCaching
     * @Import(CachingConfigurationSelector.class)
     * public @interface EnableCaching {}
     *
     * 最终会给需要使用缓存的bean船舰代理对象，并且会在代理中添加一个拦截器org.springframework.cache.interceptor.CacheInterceptor，这个类中的invoke方法是关键，会拦截所有缓存相关的目标方法的执行。
     * {@link org.springframework.cache.interceptor.CacheInterceptor}
     */

    public static class Doctor {
        private String doctorCode;
        private String sessionCode;

        public Doctor(String doctorCode, String sessionCode) {
            this.doctorCode = doctorCode;
            this.sessionCode = sessionCode;
        }

        public String getDoctorCode() {
            return doctorCode;
        }

        public void setDoctorCode(String doctorCode) {
            this.doctorCode = doctorCode;
        }

        public String getSessionCode() {
            return sessionCode;
        }

        public void setSessionCode(String sessionCode) {
            this.sessionCode = sessionCode;
        }

        @Override
        public String toString() {
            return "Doctor{" +
                    "doctorCode='" + doctorCode + '\'' +
                    ", sessionCode='" + sessionCode + '\'' +
                    '}';
        }
    }

    public static class DoctorX {
        private String doctorCode;
        private List<String> sessionCode = new ArrayList<>();

        public String getDoctorCode() {
            return doctorCode;
        }

        public void setDoctorCode(String doctorCode) {
            this.doctorCode = doctorCode;
        }

        public List<String> getSessionCode() {
            return sessionCode;
        }

        public void setSessionCode(List<String> sessionCode) {
            this.sessionCode = sessionCode;
        }

        @Override
        public String toString() {
            return "DoctorX{" +
                    "doctorCode='" + doctorCode + '\'' +
                    ", sessionCode=" + sessionCode +
                    '}';
        }
    }

    @Test
    public void test1() {
        List<Doctor> testData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            testData.add(new Doctor(""+i%10, ""+i));
        }
        Map<String, DoctorX> doctorMap = new HashMap<>();
        for (Doctor item: testData) {
            String doctorCode = item.getDoctorCode();
            String sessionCode = item.getSessionCode();
            if (doctorMap.containsKey(doctorCode)) {
                doctorMap.get(doctorCode).getSessionCode().add(sessionCode);
                continue;
            }
            DoctorX doctorX = new DoctorX();
            doctorX.setDoctorCode(doctorCode);
            doctorMap.put(doctorCode, doctorX);
            doctorMap.get(doctorCode).getSessionCode().add(sessionCode);
        }
        doctorMap.forEach((key,value)-> System.out.println(key+":"+value));
        doctorMap.values().forEach(System.out::println);
    }


    @Test
    public void test2() {
        List<Map<String, Object>> doctorList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> doctor = new HashMap<>();
            doctor.put("doctorCode", ""+(i%10));
            doctor.put("sessionCode", ""+i);
            doctorList.add(doctor);
        }
        Map<String, Map<String, Object>> doctorMap = new HashMap<>();
        for (Map<String, Object> doctor: doctorList) {
            String doctorCode = (String) doctor.get("doctorCode");
            if (doctorMap.containsKey(doctorCode)) {
                ((List) doctorMap.get(doctorCode).get("sessionCode")).add(doctor.get("sessionCode"));
                continue;
            }
            Map<String, Object> newDoctor = new HashMap<>();
            newDoctor.put("doctorCode", doctorCode);
            newDoctor.put("sessionCode", new ArrayList<>());
            ((List) newDoctor.get("sessionCode")).add(doctor.get("sessionCode"));
            doctorMap.put(doctorCode, newDoctor);
        }
        doctorMap.forEach((key,value)-> System.out.println(key+":"+value));
        doctorMap.values().forEach(System.out::println);
    }


























}
