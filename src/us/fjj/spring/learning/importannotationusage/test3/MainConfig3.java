package us.fjj.spring.learning.importannotationusage.test3;

import org.springframework.context.annotation.Import;
import us.fjj.spring.learning.importannotationusage.test3.module1.ComponentScanModule1;
import us.fjj.spring.learning.importannotationusage.test3.module2.ComponentScanModule2;

@Import({ComponentScanModule1.class, ComponentScanModule2.class})
public class MainConfig3 {
}
