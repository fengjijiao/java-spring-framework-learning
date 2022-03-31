package us.fjj.spring.learning.junitusage;

/**
 * 通常我们写完代码后，为了确保代码的正确性，都需要自己测试一遍，看一下代码的运行结果和我们期望的结果是不是一样的，也就是我们常说的单元测试，java中最常用的单元测试框架是Junit，本文主要介绍3个内容：
 * 1.玩转junit
 * 2.spring集成junit
 * 3.开发工具中使用junit
 *
 * 1.背景
 * 我们写了一个工具类，有两个方法
 *
 * 然后我们想配置这两个方法，下面我们来写测试代码， 如下，测试一下max方法和min方法的结果和我们期望的及如果是否一致，不一致的时候，输出一段文字。
 */
public class JunitTest {
    /**
     * 不用Junit的测试
     */
    public static void main(String[] args) {
        testMax();
        testMin();
    }

    public static void testMax() {
        int result = MathUtils.max(1,2,3);
        if (result != 3) {
            System.out.println(String.format("max方法有问题，期望结果是3，实际结果是%s", result));
        }
    }

    public static void testMin() {
        int result = MathUtils.min(1,2,3);
        if (result != 1) {
            System.out.println(String.format("max方法有问题，期望结果是1，实际结果是%s", result));
        }
    }

    /**
     * 上面我们要测试的方法就2个，若需测试的方法很多的时候，咱们需要写大量的这种测试代码，工作量还是蛮大的，而Junit做的事情和上面差不多，都是用来判断被测试的方法和期望的结果是否一致，不一致的时候给出提示，不过Junit用起来更容易一些，还有各种开发用到的ide(eclipse、idea)结合的使用更好一些，用起来特别的顺手。
     */


    /**
     * Junit
     * 使用步骤
     * 1.添加junit maven配置
     * 2.写测试用例，在协会的测试方法上面添加@Test注解，比如我们需要对上面案例中的max方法进行测试， 通常我们会新建一个测试类，类名为被测试的类加上Test后缀，即MathUtilsTest，然后在这个类我们需要写max方法的测试方法，如下，需要我们在max方法上面加上@Test注解。
     * //见MathUtilsTest.java
     * 3.运行测试用例，现在测试代码都写好了，下面我们写个类来启动测试用例，这里需要使用JUnitCore.runClasses方法来运行测试用例，如下：
     * //见Demo1TestRunner.java
     *
     *
     *
     * 使用断言
     * 什么是断言？
     * 断言是用来判断程序的运行结果和我们期望的姐u共是不是一致的，如果不一致，会抛出异常，断言中有3个信息比较关键
     * 1.被测试的数据
     * 2.期望的数据
     * 3.抛出异常
     * 断言提供的方法将被测试的数据和期望的数据进行比对，如果不一样的时候将抛出异常，程序可以捕获这个异常，这样就可以知道测试失败了。
     * junit中的org.junit.Assert类中提供了大量静态方法（Junit4），用来判断被测试的数据和期望的数据是否一致，不一致将抛出异常。
     */



















}
