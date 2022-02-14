package us.fjj.spring.learning.importannotationusage.test6;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MethodCostTimeProxyImportSelector.class)
public @interface EnableMethodCostTime {
}
