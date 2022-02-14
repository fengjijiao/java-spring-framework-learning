package us.fjj.spring.learning.importannotationusage.test8;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;

@Order(2)
public class DeferredImportSelector2 implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                Configuration2.class.getName()
        };
    }
}
