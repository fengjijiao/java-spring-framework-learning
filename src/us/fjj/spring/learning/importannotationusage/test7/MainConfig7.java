package us.fjj.spring.learning.importannotationusage.test7;

import org.springframework.context.annotation.Import;

@Import(
        {
                DeferredImportSelector3.class,//最后处理
                Configuration2.class,//1
                ImportSelector1.class//2
        }
)
public class MainConfig7 {
}
