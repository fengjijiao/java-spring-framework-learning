package us.fjj.spring.learning.speldemo;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class Simple {
    public static void main(String[] args) {
        //构造解析器
        ExpressionParser parser = new SpelExpressionParser();
        //解析器解析字符串表达式
        Expression expression = parser.parseExpression("'Hello, World!'");
        //获取表达式的值
        String message = (String) expression.getValue();
        System.out.println(message);
        //Hello, World!

        //调用concat方法
        Expression expression1 = parser.parseExpression("'ok, l'.concat('xh')");
        String message1 = (String) expression1.getValue();
        System.out.println(message1);
        //ok, lxh

        //调用String的属性bytes，将字符串转换成字节数组
        Expression expression2 = parser.parseExpression("'Hello'.bytes");
        byte[] bs = (byte[]) expression2.getValue();
        for (int i = 0; i < bs.length; i++) {
            System.out.println(bs[i]+" ");
        }

        //SPEL还支持嵌套属性，下面将字符串转换为字节后获取长度
        Expression expression3 = parser.parseExpression("'Hello'.bytes.length");
        int length = (int) expression3.getValue();
        System.out.println(length);

        //字符串的构造函数可以被调用，而不是使用字符串文本，下面将字符串内容转换为大写字母
        Expression expression4 = parser.parseExpression("new String('hello').toUpperCase()");
        String res = expression4.getValue(String.class);
        System.out.println(res);


        //spel表达式可以与XML或基于注解的配置元数据一起使用，spel表达式以#{开头}结尾，如#{'hello'}
        //1.基于XML配置
        //可以使用以下表达式来设置属性或构造函数的参数值
        /**
         * <bean id="number" class="net.test.Number">
         *     <property name="randomNumber" value="#{T(java.lang.Math).random()*100.0}"/>
         *     </bean>
         *
         */
        //也可以通过名称引用其他Bean属性
        /**
         * <bean id="shapeGuess" class="net.test.ShapeGuess">
         *     <property name="shapeSeed" value="#{number.randomNumber}"/>
         *     </bean>
         */
        //2.基于注解的配置
        //@Value注解可以放在字段、方法、以及构造函数的参数上，以指定默认值
        //以下是一个设置字段变量默认值的例子
        /**
         * public static class FieldValueTestBean {
         * @value("#{ systemProperties['user.region'] }")
         *  private String defaultLocale;
         *
         *  public void setDefaultLocale(String defaultLocale) {
         *  this.defaultLocale = defaultLocale;
         *  }
         *
         *  public String getDefaultLocale() {
         *  return this.defaultLocale;
         *  }
         * }
         */


        //spel中的运算符

        //算术运算符
        System.out.println(parser.parseExpression("'hello'+' you!'").getValue());
        System.out.println(parser.parseExpression("10*10/2").getValue());
        System.out.println(parser.parseExpression("'today: '+new java.util.Date()").getValue());

        //逻辑运算符
        System.out.println(parser.parseExpression("true and false").getValue());

        //关系运算符
        System.out.println(parser.parseExpression("'soons'.length()==5").getValue());


        //spel中的变量

        Calculation calculation = new Calculation();
        StandardEvaluationContext context = new StandardEvaluationContext(calculation);
        parser.parseExpression("number").setValue(context, "5");
        System.out.println(calculation.cube());


        /**
         * Hello, World!
         * ok, lxh
         * 72
         * 101
         * 108
         * 108
         * 111
         * 5
         * HELLO
         * hello you!
         * 50
         * today: Mon Nov 01 20:08:51 CST 2021
         * false
         * true
         * 125
         */
    }
}
