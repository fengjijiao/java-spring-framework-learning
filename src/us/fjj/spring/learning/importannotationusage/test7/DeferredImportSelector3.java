package us.fjj.spring.learning.importannotationusage.test7;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class DeferredImportSelector3 implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                Configuration3.class.getName()
        };
    }
}
