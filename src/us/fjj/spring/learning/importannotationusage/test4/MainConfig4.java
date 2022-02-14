package us.fjj.spring.learning.importannotationusage.test4;

import org.springframework.context.annotation.Import;

@Import(MyImportBeanDefinitionRegistrar.class)
public class MainConfig4 {
}
