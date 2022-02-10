package us.fjj.spring.learning.annotationusage;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

public class UseAnnotation7<@Ann7("T0是在类上声明的一个泛型类型变量") T0, @Ann7("T1是在类上声明的一个泛型类型变量") T1> {
    public <@Ann7("T2是在方法上声明的泛型类型变量") T2> void m1() {
    }

    public static void main(String[] args) throws NoSuchMethodException {
        for (TypeVariable typeVariable :
                UseAnnotation7.class.getTypeParameters()) {
            print(typeVariable);
        }
        for (TypeVariable typeVariable :
                UseAnnotation7.class.getDeclaredMethod("m1").getTypeParameters()) {
            print(typeVariable);
        }
        /**
         * 类型变量名称：T0
         * @us.fjj.spring.learning.annotationusage.Ann7("T0\u662f\u5728\u7c7b\u4e0a\u58f0\u660e\u7684\u4e00\u4e2a\u6cdb\u578b\u7c7b\u578b\u53d8\u91cf")
         * 类型变量名称：T1
         * @us.fjj.spring.learning.annotationusage.Ann7("T1\u662f\u5728\u7c7b\u4e0a\u58f0\u660e\u7684\u4e00\u4e2a\u6cdb\u578b\u7c7b\u578b\u53d8\u91cf")
         * 类型变量名称：T2
         * @us.fjj.spring.learning.annotationusage.Ann7("T2\u662f\u5728\u65b9\u6cd5\u4e0a\u58f0\u660e\u7684\u6cdb\u578b\u7c7b\u578b\u53d8\u91cf")
         */
    }

    private static void print(TypeVariable typeVariable) {
        System.out.println("类型变量名称：" + typeVariable);
        Arrays.stream(typeVariable.getAnnotations()).forEach(System.out::println);
    }
}
