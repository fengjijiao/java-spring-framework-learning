package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({Service1.class, Service2.class, MainRun.class})
public class MainConfig1 {
}
