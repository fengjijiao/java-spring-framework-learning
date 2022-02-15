package us.fjj.spring.learning.annotationimplementDI.other.test3;

import org.springframework.context.annotation.Import;

@Import({MyImportSelector.class})
public class MainConfig3 {
}
