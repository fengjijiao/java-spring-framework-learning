package us.fjj.spring.learning.componentscanannotation;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
        "us.fjj.spring.learning.componentscanannotation.test1.controller",
        "us.fjj.spring.learning.componentscanannotation.test1.service"
})
public class ScanBean2 {
}
