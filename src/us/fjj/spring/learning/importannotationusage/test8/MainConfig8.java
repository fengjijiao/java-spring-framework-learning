package us.fjj.spring.learning.importannotationusage.test8;

import org.springframework.context.annotation.Import;

@Import(
        {
                DeferredImportSelector2.class,//priority=2
                DeferredImportSelector3.class,//priority=1
                Configuration1.class
        }
)
public class MainConfig8 {
}
