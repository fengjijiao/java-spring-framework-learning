package us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test1;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Service2 implements IService {
}
