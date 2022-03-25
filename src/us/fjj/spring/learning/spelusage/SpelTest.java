package us.fjj.spring.learning.spelusage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.*;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import us.fjj.spring.learning.annotationdemo.UserService;

import javax.naming.ldap.PagedResultsControl;
import java.lang.reflect.Method;
import java.util.*;

/**
 * SpEL是什么？
 * spel表达式语言全称为“spring expression language”，缩写为"SpEL"，类似于Struts2x中使用的OGNL表达式语言，能在运行时构建复杂表达式、存取对象图属性、对象方法调用等等，并且能与Spring功能完美整合，如能用来配置Bean定义。
 * 表达式语言给静态Java语言增加了动态功能。
 * SpEL是单独模块，只依赖于core模块，不依赖于其他模块，可单独使用。
 *
 *
 * SpEL能干什么？
 * 表达式语言一般是用最简单的形式完成最主要的工作，减少我们的工作量。
 * SpEL支持如下表达式：
 * 一、基本表达式：字面量表达式、关系，逻辑与算数运算符表达式、字符串连接及截取表达式、三目运算及Elivis表达式、正则表达式、括号优先级表达式；
 * 二、类相关表达式：类类型表达式、类实例化、instanceof表达式、变量定义及引用、赋值表达式、自定义函数、对象属性存取及安全导航表达式、对象方法调用、Bean引用。
 * 三、集合相关表达式：内联List、内联数组、集合，字典、列表访问，字典、数组修改，集合投影、集合选择；不支持多维内联数组初始化；不支持内联字典定义；
 * 四、其他表达式：模板表达式。
 * 注：SpEL表达式中的关键字是不区分大小写的。
 */
public class SpelTest {
    /**
     * HelloWorld
     * 首先准备支持SpEL的jar包：org.springframework.expression-3.0.5.RELEASE.jar将其添加到类路径中。
     * SpEL在求表达式值时一般分为四步，其中第三步可选：首先构造一个解析器，其次解析器解析字符串表达式，在此构造上下文，最后根据上下文得到表达式运算后的值。
     *
     * 案例
     */
    @Test
    public void test1() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("('Hello' + ' World').concat(#end)");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("end", "!");
        System.out.println(expression.getValue(context));
        /**
         * Hello World!
         */
    }
    /**
     * 分析代码：
     * 1） 创建解析器：SpEL使用ExpressionParser接口表示解析器，提供SpelExpressionParser默认实现；
     * 2)  解析表达式：使用ExpressionParser的parseExpression来解析响应的表达式为Expression对象。
     * 3） 构造上下文：准备比如变量定义等等表达式需要的上下文数据。
     * 4) 求值：通过Expression接口的getValue方法根据上下文获取表达式的值。
     */
    /**
     * 工作原理
     * 1.首先定义表达式：“1+2”;
     * 2.定义解析器ExpressionParser实现，SpEL提供默认实现SpelExpressionParser;
     * 2.1.SpelExpressionParser解析器内部使用Tokenizer类进行词法分析，即包字符串流菲尼为记号流，记号在SpEL中使用Token类来表示;
     * 2.2.有了记号流后，解析器可根据记号流生成内部抽象语法树；在SpEL中语法树节点由SpELNode接口实现代表：如OpPlus表示加操作节点、IntLiteral表示int型字面量节点；使用SpELNode实现组成了抽象语法树。
     * 2.3.对外提供Expression接口来简化表示抽象语法树，从而隐藏内部实现细节，并提供getValue简单方法用于获取表达式值；SpEL提供默认实现为SpELExpression;
     * 3.定义表达式上下文对象（可选），SpEL使用EvaluationContext接口表示上下文对象，用于设置根对象、自定义变量、自定义函数，类型转换器等，SpEL提供默认实现StandardEvaluationContext；
     * 4.使用表达式对象根据上下文对象（可选）求值（调用表达式对象的getValue方法）获得结果。
     */
    /**
     * ExpressionParser接口
     * 表示解析器，默认实现是org.springframework.expression.spel.standard包中的SpELExpressionParser类
     * {@link org.springframework.expression.spel.standard.SpelExpressionParser}
     * 使用parseExpression方法将字符串表达式转换为Expression对象，对于ParserContext接口用于定义字符串表达式是不是模板，及模板开始与结束字符：
     *
     * 示例：
     */
    @Test
    public void test2() {
        ExpressionParser parser = new SpelExpressionParser();
        ParserContext parserContext = new ParserContext() {
            @Override
            public boolean isTemplate() {
                return true;
            }

            @Override
            public String getExpressionPrefix() {
                return "#{";
            }

            @Override
            public String getExpressionSuffix() {
                return "}";
            }
        };
        String template = "#{'Hello '}#{'World!'}";
        Expression expression = parser.parseExpression(template, parserContext);
        System.out.println(expression.getValue());
        /**
         * Hello World!
         */
        /**
         * 在此我们演示的是使用ParserContext的情况，此处定义了ParserContext实现：定义表达式是模块，表达式前缀为'#{'，后缀为'}'；使用parseExpression解析时传入的模板必须以'#{'开头，以'}'结尾，如"#{'Hello '}#{'World!'}"。
         * 默认传入的是字符串表达式不是模板形式，如之前演示的Hello World。
         */
    }
    /**
     * EvaluationContext接口
     * 表示上下文环境，默认实现是org.springframework.expression.spel.support包中的StandardEvaluationContext类，使用setRootObject方法来设置根对象，使用setVariable方法来注册自定义变量，使用registerFunction来注册自定义函数等等。
     *
     * Expression接口
     * 表示表达式对象，默认实现是org.springframework.expression.spel.standard包中的SpelExpression，提供getValue方法用于获取表达式值，提供setValue方法用于设置对象值。
     *
     */

    /**
     * SpEL语法
     * 基本表达式
     *
     * 字面量表达式：SpEL支持的字面量包括：字符串、数字类型（int、long、float、double）、布尔类型、null类型。
     * 类型           示例
     * 字符串          String str1 = parser.parseExpression("Hello World!").getValue(String.class);
     * 数字类型         int int1 = parser.parseExpression("1").getValue(Integer.class);
     *                 long long1 = parser.parseExpression("-1L").getValue(Long.class);
     *                 float float1 = parser.parseExpression("1.1").getValue(Float.class);
     *                 double double1 = parser.parseExpression("1.1E+2").getValue(Double.class);
     *                 int hex1 = parser.parseExpression("0xa").getValue(Integer.class);
     *                 long hex2 = parser.parseExpression("0xaL").getValue(Long.class);
     * 布尔类型         boolean true1 = parser.parseExpression("true").getValue(boolean.class);
     *                 boolean false1 = parser.parseExpression("false").getValue(boolean.class);
     * null类型       Object null1 = parser.parseExpression("null").getValue(Object.class);
     */
    @Test
    public void test3() {
        SpelExpressionParser parser = new SpelExpressionParser();
        String str1 = parser.parseExpression("'Hello World!'").getValue(String.class);//注意：有个''
        int int1 = parser.parseExpression("1").getValue(Integer.class);
        long long1 = parser.parseExpression("-1L").getValue(Long.class);
        float float1 = parser.parseExpression("1.1").getValue(Float.class);
        double double1 = parser.parseExpression("1.1E+2").getValue(Double.class);
        int hex1 = parser.parseExpression("0xa").getValue(Integer.class);
        long hex2 = parser.parseExpression("0xaL").getValue(Long.class);
        boolean true1 = parser.parseExpression("true").getValue(boolean.class);
        boolean false1 = parser.parseExpression("false").getValue(boolean.class);
        Object null1 = parser.parseExpression("null").getValue(Object.class);
        System.out.println("str1: "+ str1);
        System.out.println("int1: "+ int1);
        System.out.println("long1: "+ long1);
        System.out.println("float1: "+ float1);
        System.out.println("double1: "+ double1);
        System.out.println("hex1: "+ hex1);
        System.out.println("hex2: "+ hex2);
        System.out.println("true1: "+ true1);
        System.out.println("false1: "+ false1);
        System.out.println("null1: "+ null1);
        /**
         * str1: Hello World!
         * int1: 1
         * long1: -1
         * float1: 1.1
         * double1: 110.0
         * hex1: 10
         * hex2: 10
         * true1: true
         * false1: false
         * null1: null
         */
    }

    /**
     * 算术运算表达式
     * SpEL支持加（+）、减（-）、乘（*）、除（/）、求余（%）、幕（^）运算。
     * 类型           示例
     * 加减乘除         int result1 = parser.parseExpression("1+2-3*4/2").getValue(Integer.class);//-1
     * 求余             int result2 = parser.parseExpression("4%3").getValue(Integer.class);//1
     * 幕运算             int result3 = parser.parseExpression("2^3").getValue(Integer.class);//8
     * SpEL还提供求余（MOD）和除(DIV)外两个运算符，与%和/等价，不区分大小写。
     */
    @Test
    public void test4() {
        SpelExpressionParser parser = new SpelExpressionParser();
        int result1 = parser.parseExpression("1+2-3*4/2").getValue(Integer.class);
        int result2 = parser.parseExpression("4%3").getValue(Integer.class);
        int result3 = parser.parseExpression("2^3").getValue(Integer.class);
        System.out.println("1+2-3*4/2="+result1);
        System.out.println("4%3="+result2);
        System.out.println("2^3="+result3);
        /**
         * 1+2-3*4/2=-3
         * 4%3=1
         * 2^3=8
         */
    }

    /**
     * 关系表达式
     * 等于(==)、不等于(!=)、大于（>）、大于等于（>=）、小于（<）、小于等于（<=）、区间（between）运算。
     * 如parser.parseExpression("1>2").getValue(boolean.class);将返回false;
     * 而parser.parseExpression("1 between {1, 2}").getValue(boolean.class);将返回true。
     * between运算符右边操作数必须是列表类型，且只能包含2个元素。第一个元素为开始，第二个元素为结束，区间运算是包含边界值的，即：xxx >= list.get(0) && xxx <= list.get(1)。
     * SpEL提供了等价的 EQ、   NE、  GT、   GE、  LT、   LE   来表示
     *               等于、不等于、大于、大于等于、小于、小于等于  。
     */
    @Test
    public void test5() {
        ExpressionParser parser = new SpelExpressionParser();
        boolean v1 = parser.parseExpression("1>2").getValue(boolean.class);
        boolean between1 = parser.parseExpression("1 between {1,2}").getValue(boolean.class);
        System.out.println("1>2="+v1);
        System.out.println("1 between {1,2}="+between1);
    }

    /**
     * 逻辑表达式
     * 且（and/&&）、或（or/||）、非（!或NOT）；
     *
     */
    @Test
    public void test6() {
        SpelExpressionParser parser = new SpelExpressionParser();
        boolean result1 = parser.parseExpression("2>1 && (!true or !false)").getValue(boolean.class);
        boolean result2 = parser.parseExpression("2>1 && (!true || !false)").getValue(boolean.class);
        boolean result3 = parser.parseExpression("2>1 && (NOT true or NOT false)").getValue(boolean.class);
        boolean result4 = parser.parseExpression("2>1 && (NOT true || NOT false)").getValue(boolean.class);

        System.out.println("result1="+result1);
        System.out.println("result2="+result2);
        System.out.println("result3="+result3);
        System.out.println("result4="+result4);
        /**
         * result1=true
         * result2=true
         * result3=true
         * result4=true
         */
    }

    /**
     * 字符串连接及截取表达式
     * 使用+进行字符串连接，使用"String[0][index]"来截取一个字符，目前只支持截取一个，如“'Hello'+'World!'”得到"Hello World!"；而“'Hello Worold!'[0]”将返回H。
     */

    /**
     * 三目运算符
     * 三目运算符“表达式1？表达式2：表达式3”用于构造三目运算表达式。
     *
     * Elivis运算符
     * Elivis运算符“表达式1?:表达式2”从Groovy语言引入用于简化三目运算符的，当表达式1为非null时则返回表达式1，当表达式1为null时则返回表达式2，简化了三目运算符表达式”表达式1？表达式1：表达式2“，
     * 如null?:false将返回false,true?:true将返回true;
     *
     * 正则表达式
     * 使用“str matches regex”，如“'123' matches '\d{3}'”将返回true；
     *
     * 括号优先级表达式
     * 使用“（表达式）”构造，括号里的具有高优先级。
     *
     *
     */
    @Test
    public void test7() {
        SpelExpressionParser parser = new SpelExpressionParser();
        String str1 = parser.parseExpression("'Hello'+'World!'").getValue(String.class);
        System.out.println("'Hello'+'World！' -> "+str1);
        String str2 = parser.parseExpression("'Hello World!'[0]").getValue(String.class);
        System.out.println("'Hello World!'[0] -> "+str2);
        String str3 = parser.parseExpression("2>1?'Hello':'World'").getValue(String.class);
        System.out.println("2>1?'Hello':'World' -> "+str3);
        boolean bl0 = parser.parseExpression("true?:false").getValue(boolean.class);
        System.out.println("true?:false -> "+bl0);
        boolean bl1 = parser.parseExpression("'339' matches '\\d{3}'").getValue(boolean.class);
        System.out.println("'339' matches '\\d{3}' -> "+bl1);
        int in1 = parser.parseExpression("(9+9)*3").getValue(int.class);
        System.out.println("(9+9)*3="+in1);
        /**
         * 'Hello'+'World！' -> HelloWorld!
         * 'Hello World!'[0] -> H
         * 2>1?'Hello':'World' -> Hello
         * true?:false -> true
         * '339' matches '\d{3}' -> true
         * (9+9)*3=54
         */
    }

    /**
     * 类相关表达式
     * 类类型表达式
     * 使用"T(Type)"来表示java.lang.Class实例，“Type”必须是类全限定名，“java.lang”包除外，即该包下的类可以不指定包名；使用类类型表达式还可以进行访问类静态方法及类静态字段。
     *
     */
    @Test
    public void test8() {
        ExpressionParser parser = new SpelExpressionParser();
        //java.lang包类访问
        Class<String> result1 = parser.parseExpression("T(String)").getValue(Class.class);
        System.out.println(result1);
        //其他包类访问
        String expression2 = "T(us.fjj.spring.learning.annotationdemo.UserService)";
        Class<UserService> value = parser.parseExpression(expression2).getValue(Class.class);
        System.out.println(value == UserService.class);
        //类静态字段访问
        String f1 = parser.parseExpression("T(us.fjj.spring.learning.spelusage.CConstant).f1").getValue(String.class);
        assert f1 != null;
        System.out.println(f1.equals(CConstant.f1));
        //类静态方法访问
        String m1 = parser.parseExpression("T(us.fjj.spring.learning.spelusage.CConstant).m1('ok')").getValue(String.class);
        System.out.println(m1.equals(CConstant.m1("ok")));
        /**
         * class java.lang.String
         * true
         * true
         * true
         */
    }

    /**
     * 类实例化
     *
     * 类实例化同样使用java关键字'new'，类名必须是全限定名，但java.lang包内的类型除外，如String、Integer.
     *
     */
    @Test
    public void test9() {
        ExpressionParser parser = new SpelExpressionParser();
        String result1 = parser.parseExpression("new String('laokai')").getValue(String.class);
        System.out.println(result1);

        Date result2 = parser.parseExpression("new java.util.Date()").getValue(Date.class);
        System.out.println(result2);

        /**
         *laokai
         * Wed Mar 23 14:12:24 CST 2022
         */
    }

    /**
     * instanceof表达式
     * SpEL支持instanceof运算符，即java内使用同义；如”'haha' instanceof T(String)“将返回true。
     *
     */
    @Test
    public void test10() {
        ExpressionParser parser = new SpelExpressionParser();
        boolean value = parser.parseExpression("'ok' instanceof T(String)").getValue(boolean.class);
        System.out.println(value);
        /**
         * true
         */
    }


    /**
     * 变量定义及引用
     * 变量定义通过EvaluationContext接口的setVariable(variableName, value)方法定义；在表达式中使用#variableName引用；除了引用自定义变量，SpEL还允许引用根对象及当前上下文对象，使用#root引用根对象，使用#this引用当前上下文对象;
     *
     */
    @Test
    public void test11() {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("name", "follow");
        context.setVariable("lesson", "ok!");

        //获取name变量，lesson变量
        String name = parser.parseExpression("#name").getValue(context, String.class);
        System.out.println(name);
        String lesson = parser.parseExpression("#lesson").getValue(context, String.class);
        System.out.println(lesson);

        //StandardEvaluationContext构造器传入root对象，可以通过#root来访问root对象
        context = new StandardEvaluationContext("我是root对象");
        String rootObj = parser.parseExpression("#root").getValue(context, String.class);
        System.out.println(rootObj);

        //#this用来访问当前上下文中的跪下
        String thisObj = parser.parseExpression("#this").getValue(context, String.class);
        System.out.println(thisObj);

        /**
         * follow
         * ok!
         * 我是root对象
         * 我是root对象
         */

        /**
         * 使用#variable来引用EvaluationContext定义的变量；除了可以引用自定义变量，还可以使用#root引用根对象，#this引用当前上下文对象，此处#this即根对象。
         */
    }

    /**
     * 自定义函数
     */
    /**
     * 目前只支持类静态方法注册为自定义函数；SpEL使用StandardEvaluationContext的registerFunction方法进行注册自定义函数，其实完全可以使用setVariable代替，两者其实本质是一样的；
     */
    @Test
    public void test12() throws NoSuchMethodException {
        //定义2个函数，registerFunction和setVariable都可以，不过从语义上来看用registerFunction更恰当
        StandardEvaluationContext context = new StandardEvaluationContext();
        Method parseInt = Integer.class.getDeclaredMethod("parseInt", String.class);
        context.registerFunction("parseInt1", parseInt);
        context.setVariable("parseInt2", parseInt);

        ExpressionParser parser = new SpelExpressionParser();
        System.out.println(parser.parseExpression("#parseInt1('3')").getValue(context, int.class));
        System.out.println(parser.parseExpression("#parseInt2('3')").getValue(context, int.class));

        String expression1 = "#parseInt1('3') == #parseInt2('3')";
        boolean result1 = parser.parseExpression(expression1).getValue(context, boolean.class);
        System.out.println(result1);

        /**
         * 3
         * 3
         * true
         */

        /**
         * 此处可以看出"registerFunction"和"setVariable"都可以注册自定义函数，但是两个方法的含义不一样，推荐使用registerFunction方法注册自定义函数。
         */

    }

    /**
     * 表达式赋值
     * 使用Expression#setValue方法可以给表达式赋值
     */
    @Test
    public void test13() {
        Object user = new Object() {
            private String name;

            @Override
            public String toString() {
                return "$classname{" +
                        "name='" + name + '\'' +
                        '}';
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        };
        {
            //user为root对象
            ExpressionParser parser = new SpelExpressionParser();
            EvaluationContext context = new StandardEvaluationContext(user);
            parser.parseExpression("#root.name").setValue(context, "ok!");
            System.out.println(parser.parseExpression("#root").getValue(context, user.getClass()));
        }
        {
            //user为变量
            ExpressionParser parser = new SpelExpressionParser();
            EvaluationContext context = new StandardEvaluationContext();
            context.setVariable("user", user);
            parser.parseExpression("#user.name").setValue(context, "ok!");
            System.out.println(parser.parseExpression("#user").getValue(context, user.getClass()));
        }

        /**
         * $classname{name='ok!'}
         * $classname{name='ok!'}
         */
    }

    /**
     * 对象属性存取及安全导航表达式
     * 对象属性获取非常简单，即使用如“a.property.property”这种点缀式获取，SpEl对于属性名首字母是不区分大小写的；SpEL还引入了Groovy语言中的安全导航运算符“(对象|属性)?.属性”，用来避免“？.”前边的表达式为null时抛出空指针异常，而是返回null；修改对象属性值则可以通过赋值表达式或Expression接口的setValue方法修改。
     */
    public static class Car {
        private String name;

        @Override
        public String toString() {
            return "Car{" +
                    "name='" + name + '\'' +
                    '}';
        }

        public Car() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Car(String name) {
            this.name = name;
        }
    }
    public static class User {
        private Car car;

        public User() {
        }

        public User(Car car) {
            this.car = car;
        }

        @Override
        public String toString() {
            return "User{" +
                    "car=" + car +
                    '}';
        }

        public Car getCar() {
            return car;
        }

        public void setCar(Car car) {
            this.car = car;
        }
    }

    @Test
    public void test14() {
        User user = new User();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", user);
        ExpressionParser parser = new SpelExpressionParser();
        //使用.符号，访问user.car.name会报错，原因：user.car为空
        try {
            System.out.println(parser.parseExpression("#user.car.name").getValue(context, String.class));
        } catch (EvaluationException | ParseException e) {
            System.out.println("出错了："+e.getMessage());
        }
        //使用安全访问符?.，可以规避null错误。
        System.out.println(parser.parseExpression("#user?.car?.name").getValue(context, String.class));

        Car car = new Car();
        car.setName("ok!");
        user.setCar(car);

        System.out.println(parser.parseExpression("#user?.car?.name").getValue(context, String.class));
        /**
         * 出错了：EL1007E: Property or field 'name' cannot be found on null
         * null
         * ok!
         */
    }

    /**
     * 对象方法调用
     * 对象方法方法调用更简单，跟Java语法一样；如“'hash'.substring(2,4)”将返回“sh”；而对于根对象可以直接调用方法；
     */
    @Test
    public void test15() {
        ExpressionParser parser = new SpelExpressionParser();
        String result = parser.parseExpression("'hash'.substring(2,4)").getValue(String.class);
        System.out.println(result);
    }

    /**
     * Bean引入
     * SpEL支持使用@符号来引用Bean，在引用Bean时需要使用BeanResolver接口实现来查找Bean，spring提供BeanFactoryResolver实现。
     *
     */
    @Test
    public void test16() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        User user = new User();
        Car car = new Car();
        car.setName("保时捷");
        user.setCar(car);
        factory.registerSingleton("user", user);

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(factory));

        ExpressionParser parser = new SpelExpressionParser();
        User userBean = parser.parseExpression("@user").getValue(context, User.class);
        System.out.println(userBean);
        System.out.println(userBean == factory.getBean("user"));
        /**
         * User{car=Car{name='保时捷'}}
         * true
         */
    }

    /**
     * 集合相关表达式
     *
     * 内联List
     * 从Spring3.0.4开始支持内联List，使用{表达式，...}定义内联List，如{1,2,3}将返回一个整型的ArrayList，而{}将返回空的List，对于字面量表达式列表，SpEL会使用java.util.Collections.unmodifiableList方法将列表设置为不可修改。
     *
     */
    @Test
    public void test17() {
        ExpressionParser parser = new SpelExpressionParser();
        //将返回不可修改的空list
        List<Integer> result1 = parser.parseExpression("{}").getValue(List.class);
        //对于字面量列表也将返回不可修改的List
        List<Integer> result2 = parser.parseExpression("{1,2,3}").getValue(List.class);
        System.out.println(new Integer(1).equals(result2.get(0)));
        try {
            result2.set(0, 2);
        }catch (Exception e) {
            e.printStackTrace();
        }
        //对于列表中只要有一个不是字面量表达式，将只返回原始list
        //不会进行不可修改处理
        String expression3 = "{{1+2,2+4},{3,4+4}}";
        List<List<Integer>> result3 = parser.parseExpression(expression3).getValue(List.class);
        result3.get(0).set(0, 2);
        System.out.println(result3);
        //声明二维数组并初始化(not supported)
//        int[][] result4 = parser.parseExpression("new int[2][3]{{1,2,9},{4,9,10}}").getValue(int[][].class);
//        System.out.println(result4[0]);
        //定义一维数组并初始化
        int[] result5 = parser.parseExpression("new int[1]").getValue(int[].class);
        System.out.println(result5[0]);
        /**
         *true
         * java.lang.UnsupportedOperationException
         * 	at java.base/java.util.Collections$UnmodifiableList.set(Collections.java:1349)
         * 	at us.fjj.spring.learning.spelusage.SpelTest.test17(SpelTest.java:607)
         * 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
         * 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
         * 	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
         * 	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
         * 	at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:688)
         * 	at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:131)
         * 	at org.junit.jupiter.engine.extension.TimeoutExtension.intercept(TimeoutExtension.java:149)
         * 	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(TimeoutExtension.java:140)
         * 	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(TimeoutExtension.java:84)
         * 	at org.junit.jupiter.engine.execution.ExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0(ExecutableInvoker.java:115)
         * 	at org.junit.jupiter.engine.execution.ExecutableInvoker.lambda$invoke$0(ExecutableInvoker.java:105)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$InterceptedInvocation.proceed(InvocationInterceptorChain.java:106)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(InvocationInterceptorChain.java:64)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(InvocationInterceptorChain.java:45)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(InvocationInterceptorChain.java:37)
         * 	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:104)
         * 	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:98)
         * 	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeTestMethod$6(TestMethodTestDescriptor.java:210)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeTestMethod(TestMethodTestDescriptor.java:206)
         * 	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:131)
         * 	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:65)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:139)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:129)
         * 	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:127)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:126)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:84)
         * 	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
         * 	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:143)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:129)
         * 	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:127)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:126)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:84)
         * 	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
         * 	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:143)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:129)
         * 	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:127)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:126)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:84)
         * 	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:32)
         * 	at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57)
         * 	at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:51)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:108)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:88)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:54)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:67)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:52)
         * 	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:96)
         * 	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:75)
         * 	at com.intellij.junit5.JUnit5IdeaTestRunner.startRunnerWithArgs(JUnit5IdeaTestRunner.java:71)
         * 	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
         * 	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:235)
         * 	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:54)
         * [[2, 6], [3, 8]]
         * 0
         */
    }

    /**
     * 集合，字典元素访问
     * SpEL目前支持所有集合类型和字典类型的元素访问，使用“集合[索引]”访问集合元素，使用“map[key]”访问字典元素;
     *
     */
    @Test
    public void test18() {
        ExpressionParser parser = new SpelExpressionParser();
       //SpEL内联List访问
       int result1 = parser.parseExpression("{1,2,3}[0]").getValue(int.class);
        System.out.println(result1);
        //SpEL目前支持所有集合类型的访问
        Collection<Integer> collection = new HashSet<>();
        collection.add(1);
        collection.add(2);

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("collection", collection);
        int result2 = parser.parseExpression("#collection[1]").getValue(context, int.class);
        System.out.println(result2);

        //SpEL对Map字典元素访问的支持
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        EvaluationContext context2 = new StandardEvaluationContext();
        context2.setVariable("map", map);
        int result3 = parser.parseExpression("#map['a']").getValue(context2, int.class);
        System.out.println(result3);
        /**
         * 1
         * 2
         * 1
         */
    }

    /**
     * 列表，字典，数组元素修改
     * 可以使用赋值表达式或Expression接口的setValue方法修改；
     */
    @Test
    public void test19() {
        ExpressionParser parser = new SpelExpressionParser();
        //修改list元素
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("collection", list);
        parser.parseExpression("#collection[0]").setValue(context, 3);
        int result1 = parser.parseExpression("#collection[0]").getValue(context, int.class);
        System.out.println(result1);

        //修改map元素值
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        EvaluationContext context2 = new StandardEvaluationContext();
        context2.setVariable("map", map);
        parser.parseExpression("#map['a']").setValue(context2, 9);
        int result2 = parser.parseExpression("#map['a']").getValue(context2, int.class);
        System.out.println(result2);
        /**
         * 3
         * 9
         */
    }

    /**
     * 集合投影
     * 在SQL中投影指从表中选择出列，而在SpEL指根据集合中的元素中通过选择来构造另一个集合，该集合和原集合具有相同数量的元素；SpEL使用"(list|map).![投影表达式]"来进行投影运算：
     */
    @Test
    public void test20() {
        ExpressionParser parser = new SpelExpressionParser();
        //1.测试集合或数组
        List<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(5);
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("list", list);
        Collection<Integer> result1 = parser.parseExpression("#list.![#this+1]").getValue(context, Collection.class);
        result1.forEach(System.out::println);
        System.out.println("------------------");
        //2.测试字典
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        EvaluationContext context1 = new StandardEvaluationContext();
        context1.setVariable("map", map);
        List<Integer> result2 = parser.parseExpression("#map.![value+1]").getValue(context1, List.class);
        result2.forEach(System.out::println);
        /**
         * 5
         * 6
         * ------------------
         * 2
         * 3
         */
    }

    /**
     * 对于集合或数组使用如上表达式进行投影运算，其中投影表达式中#this代表每个集合或数组元素，可以使用比如#this.property来获取集合元素的属性，其中#this可以省略。
     * Map投影最终只能得到List结果，如上所示，对于投影表达式指定#this将是Map.Entry，所以可以使用value来获取值，用key来获取键。
     */


    /**
     * 集合选择
     *在SQL中指使用select进行选择行数据，而在SpEL指根据原集合通过条件表达式选择出满足条件的元素并构造为新的集合，SpEL使用“(list|map).?[选择表达式]”，其中选择表达式结果必须是boolean类型，如果true则选择的元素将添加到新集合中，false将不添加到新集合中。
     *
     */
    @Test
    public void test21() {
        ExpressionParser parser = new SpelExpressionParser();
        //1.测试集合或数组
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("list", list);
        Collection<Integer> result = parser.parseExpression("#list.?[#this>=4]").getValue(context, Collection.class);
        result.forEach(System.out::println);
        /**
         * 4
         * 5
         * 6
         */
        /**
         * 对于集合或数组选择，如#collection.?[#this>4]将选择出集合元素值大于4的所有元素。选择表达式必须返回布尔类型，使用#this表示当前元素。
         */
        System.out.println("-------------");
        //2.测试字典
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        EvaluationContext context1 = new StandardEvaluationContext();
        context1.setVariable("map", map);
        Map<String, Integer> result2 = parser.parseExpression("#map.?[key!='a']").getValue(context1, Map.class);
        result2.forEach((key, value) -> {
            System.out.println(key+":"+value);
        });
        System.out.println("------------");
        List<Integer> result3 = parser.parseExpression("#map.?[key!='a'].![value+1]").getValue(context1, List.class);
        result3.forEach(System.out::println);
        /**
         * 4
         * 5
         * 6
         * -------------
         * b:2
         * c:3
         * ------------
         * 3
         * 4
         */
        /**
         * 对于字典选择，如“#map.?[#this.key!='a']”将选择键值不等于“a”的，其中选择表达式中#this是Map.Entry类型，而最终结果还是Map，这点和投影不同；集合选择和投影可以一起使用，如“#map.?[key!='a'].![value+1]”将首先选择键不等于'a'的，然后在选出的Map中再进行“value+1”的投影。
         */
    }



    /**
     * 表达式模板
     *
     * 模板表达式就是由字面量与一个或多个表达式块组成。每个表达式块由“前缀+表达式+后缀”形式组成，如“${1+2}”即表达式块。再前边已经介绍过使用ParserContext接口实现来自定义表达式是否是模板及前缀和后缀定义。如“Error ${#vo} ${#v1}”表达式表示由字面量“Error ”、模板表达式“#v0”、模板表达式“#v1”组成，其中v0和v1表示自定义变量，需要再上下文中定义。
     *
     * 解析表达式的时候需要指定模板，模板由ParserContext接口来定义。
     * 它有个子类：TemplateParserContext。
     */
    @Test
    public void test22() {
        //创建解析器
        ExpressionParser parser = new SpelExpressionParser();
        //创建解析器上下文(context)
        ParserContext context = new TemplateParserContext("%{", "}");
        Expression expression = parser.parseExpression("你好：%{#name}，我们正在学习：%{#lesson}", context);
        //创建表达式计算上下文(context1)
        EvaluationContext context1 = new StandardEvaluationContext();
        context1.setVariable("name", "fjj");
        context1.setVariable("lesson", "java spring");
        //获取值
        String value = expression.getValue(context1, String.class);
        System.out.println(value);
        /**
         * 你好：fjj，我们正在学习：java spring
         */
    }

    /**
     * 在Bean定义中使用SpEL表达式
     * xml风格的配置
     * SpEL支持在Bean定义时注入，默认使用“#{SpEL表达式}”表示，其中“#root”根对象默认可以认为是ApplicationContext，只有ApplicationContext实现默认支持SpEL，获取根对象属性其实是获取容器中的Bean。
     *
     */
    @Test
    public void test23() {
        //见同目录下的bean.xml
    }

    /**
     * 模板默认以前缀“#{“开头，以后缀”}”结尾，且不允许嵌套，如“#{'Hello'#{world}}”错误，如“#{'Hello'+world}”中“world”默认解析为Bean。当然也可以使用“@bean”引用了。
     *
     */

    /**
     * 除了XML配置方式，Spring还提供一种注解方式@Value，接着往下看把。
     */

    /**
     * 注解风格的配置
     * 基于注解风格的SpEL配置也非常简单，使用@Value注解来指定SpEL表达式，该注解可以放到字段、方法及方法参数上。
     *
     * 测试Bean类如下，使用@Value来指定SpEL表达式：
     *
     */
    @Configuration
    public static class SpELBean {
        @Bean
        public String world() {
            return "world";
        }
        @Value("#{'Hello' + world}")
        private String value;
    }


    /**
     * 在Bean定义中SpEL的问题
     * 如果有同学问“#{我不是SpEL表达式}”不是SpEL表达式，而是公司内部的模板，想换个前缀和后缀该如何实现呢？
     * 我们使用BeanFactoryPostProcessor接口提供postProcessBeanFactory回调方法，他是在IoC容器创建好但还未进行任何Bean初始化时被ApplicationContext实现调用，因此在这个阶段把SpEL前缀及后缀修改掉是安全的。
     */
    @Component
    public static class LessonModel {
        @Value("你好，%{@name}，%{@msg}")
        private String desc;

        @Override
        public String toString() {
            return "LessonModel{" +
                    "desc='" + desc + '\'' +
                    '}';
        }
    }

    /**
     * @name: 容器中name的bean
     * @msg： 容器中msg的bean
     * 下面我们来个配置类，顺便定义name和msg这个2个bean，顺便扫描上面2个配置类
     */
    @ComponentScan//比加
    @Configuration
    public static class MainConfig {
        @Bean
        public String name() {
            return "ok";
        }

        @Bean
        public String msg() {
            return "nnn";
        }
    }

    @Test
    public void test24() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig.class);
        context.refresh();
        LessonModel lessonModel = context.getBean(LessonModel.class);
        System.out.println(lessonModel);
        /**
         * LessonModel{desc='你好，%{@name}，%{@msg}'}
         */
    }

    /**
     * 总结
     *
     * 1.Spel功能还是比较强大的，可以脱离Spring环境独立运行
     * 2.Spel可以用来一些动态规则的匹配方面，比如监控系统中监控规则的动态匹配；其他的一些条件动态判断等等
     */


    @Test
    public void testSpeed() {
        Set<String> hashSet = new HashSet<>();
        Set<String> treeSet = new TreeSet<>();
        for (int i = 0; i < 10000; i++) {
            hashSet.add("K"+i);
            treeSet.add("K"+i);
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10000; i++) {
            treeSet.contains("K"+i);
        }
        stopWatch.stop();
        stopWatch.start();
        for (int i = 0; i < 10000; i++) {
            hashSet.contains("K"+i);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }















}
