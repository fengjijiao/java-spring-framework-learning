package us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("tag2")
public class Service3 implements IService {
}
