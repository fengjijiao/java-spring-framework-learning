package us.fjj.spring.learning.annotationusage;


import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;

public class AnnotationUsageTest {
    public static void main(String[] args) throws NoSuchFieldException {
        //ann9&ann9_1

        //解析类上的注解
        for (Annotation annotation:
             UseAnnotation9.class.getAnnotations()) {
            System.out.println(annotation);
        }
        /**
         * @us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u7c7b\u4e0a")
         * @us.fjj.spring.learning.annotationusage.Ann9_1(0)
         */

        //解析类上变量类型注解<>
        TypeVariable<Class<UseAnnotation9>>[] typeParameters = UseAnnotation9.class.getTypeParameters();
        for (TypeVariable<Class<UseAnnotation9>> typeParameter:
                typeParameters) {
            System.out.println(typeParameter.getName()+"变量类型注解信息：");
            for (Annotation annotation:
                 typeParameter.getAnnotations()) {
                System.out.println(annotation);
            }
        }
        /**
         * V1变量类型注解信息：
         * @us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u7c7b\u53d8\u91cf\u7c7b\u578bV1\u4e0a")
         * @us.fjj.spring.learning.annotationusage.Ann9_1(1)
         * V2变量类型注解信息：
         * @us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u7c7b\u53d8\u91cf\u7c7b\u578bV2\u4e0a")
         * @us.fjj.spring.learning.annotationusage.Ann9_1(2)
         */

        //解析字段name上的注解
        Field fieldName = UseAnnotation9.class.getDeclaredField("name");
        for (Annotation annotation:
             fieldName.getAnnotations()) {
            System.out.println(annotation);
        }
        /**
         * @us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u5b57\u6bb5\u4e0a")
         * @us.fjj.spring.learning.annotationusage.Ann9_1(3)
         */

        //解析泛型字段map上的注解
        Field fieldMap = UseAnnotation9.class.getDeclaredField("map");
        /**
         * getGenericType
         * public Type getGenericType()
         * Returns a Type object that represents the declared type for the field represented by this Field object.
         * If the Type is a parameterized type, the Type object returned must accurately reflect the actual type parameters used in the source code.
         *
         * If the type of the underlying field is a type variable or a parameterized type, it is created. Otherwise, it is resolved.
         *
         * Returns:
         * a Type object that represents the declared type for the field represented by this Field object
         * Throws:
         * GenericSignatureFormatError - if the generic field signature does not conform to the format specified in The Java™ Virtual Machine Specification
         * TypeNotPresentException - if the generic type signature of the underlying field refers to a non-existent type declaration
         * MalformedParameterizedTypeException - if the generic signature of the underlying field refers to a parameterized type that cannot be instantiated for any reason
         * Since:
         * https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Field.html#getGenericType--
         */
        //通用类型相关 start
        Type genericType = fieldMap.getGenericType();//返回表示该Field对象声明类型的Type对象（不带注解）。
        System.out.println(genericType);//java.util.Map<java.lang.String, java.lang.Integer>
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();//获取实际类型参数
        Arrays.stream(actualTypeArguments).forEach(System.out::println);
        /**
         * class java.lang.String
         * class java.lang.Integer
         */
        //通用类型相关 end
        //带注解类型相关 start
        AnnotatedType annotationType = fieldMap.getAnnotatedType();//getAnnotatedType表示带注解的Type对象。
        System.out.println(annotationType);//java.util.Map<@us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u6cdb\u578b\u7c7b\u578b\u4e0a\uff0cString") @us.fjj.spring.learning.annotationusage.Ann9_1(4) java.lang.String, @us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u6cdb\u578b\u7c7b\u578b\u4e0a\uff0cInteger") @us.fjj.spring.learning.annotationusage.Ann9_1(5) java.lang.Integer>
        AnnotatedType[] annotatedActualTypeArguments = ((AnnotatedParameterizedType) annotationType).getAnnotatedActualTypeArguments();//获取（带注解的）实际参数类型
        Arrays.stream(annotatedActualTypeArguments).forEach(System.out::println);
        /**
         * @us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u6cdb\u578b\u7c7b\u578b\u4e0a\uff0cString") @us.fjj.spring.learning.annotationusage.Ann9_1(4) java.lang.String
         * @us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u6cdb\u578b\u7c7b\u578b\u4e0a\uff0cInteger") @us.fjj.spring.learning.annotationusage.Ann9_1(5) java.lang.Integer
         */
        //带注解类型相关 end
        int i = 0;
        for (AnnotatedType actualTypeArgument:
             annotatedActualTypeArguments) {
            Type actualTypeArgument1 = actualTypeArguments[i++];
            System.out.println(actualTypeArgument1.getTypeName()+"类型上的注解如下：");
            for (Annotation annotation:
                    actualTypeArgument.getAnnotations()) {
                System.out.println(annotation);
            }
        }
        /**
         * java.lang.String类型上的注解如下：
         * @us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u6cdb\u578b\u7c7b\u578b\u4e0a\uff0cString")
         * @us.fjj.spring.learning.annotationusage.Ann9_1(4)
         * java.lang.Integer类型上的注解如下：
         * @us.fjj.spring.learning.annotationusage.Ann9("\u7528\u5728\u4e86\u6cdb\u578b\u7c7b\u578b\u4e0a\uff0cInteger")
         * @us.fjj.spring.learning.annotationusage.Ann9_1(5)
         */
    }
}
