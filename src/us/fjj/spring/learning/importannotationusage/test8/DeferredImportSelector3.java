package us.fjj.spring.learning.importannotationusage.test8;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;

public class DeferredImportSelector3 implements DeferredImportSelector, Ordered {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                Configuration3.class.getName()
        };
    }

    @Override
    public int getOrder() {
        return 1;
    }//越小越优先
}
