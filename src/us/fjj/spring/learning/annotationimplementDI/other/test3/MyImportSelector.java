package us.fjj.spring.learning.annotationimplementDI.other.test3;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import us.fjj.spring.learning.annotationimplementDI.other.test3.module1.ModuleConfig1;
import us.fjj.spring.learning.annotationimplementDI.other.test3.module2.ModuleConfig2;

public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                ModuleConfig1.class.getName(),
                ModuleConfig2.class.getName()
        };
    }
}
