package us.fjj.spring.learning.importannotationusage.test6;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MethodCostTimeProxyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                MethodCostTimeProxyBeanPostProcessor.class.getName()
        };
    }
}
