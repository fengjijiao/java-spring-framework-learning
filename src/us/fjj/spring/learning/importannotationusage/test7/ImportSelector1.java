package us.fjj.spring.learning.importannotationusage.test7;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ImportSelector1 implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                Configuration1.class.getName()
        };
    }
}
