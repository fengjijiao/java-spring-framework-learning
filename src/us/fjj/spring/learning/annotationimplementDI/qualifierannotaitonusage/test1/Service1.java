package us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("tag1")
public class Service1 implements IService {
}
