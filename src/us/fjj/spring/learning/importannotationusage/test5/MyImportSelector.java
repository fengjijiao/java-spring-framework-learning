package us.fjj.spring.learning.importannotationusage.test5;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String[] result = new String[] {
                Service1.class.getName(),
                Module1Config.class.getName()
        };
        System.out.println(Arrays.toString(result));
        /**
         * [us.fjj.spring.learning.importannotationusage.test5.Service1,
         * us.fjj.spring.learning.importannotationusage.test5.Module1Config]
         */
        return result;
    }
}
