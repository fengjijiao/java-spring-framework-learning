package us.fjj.spring.learning.componentscanannotation.test2;

import org.springframework.context.annotation.ComponentScan;
import us.fjj.spring.learning.componentscanannotation.test2.t.ScanClass;

@ComponentScan(basePackageClasses = ScanClass.class)
public class ScanBean3 {
}
