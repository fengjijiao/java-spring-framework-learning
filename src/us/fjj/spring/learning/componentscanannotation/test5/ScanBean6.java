package us.fjj.spring.learning.componentscanannotation.test5;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(
        basePackages = {"us.fjj.spring.learning.componentscanannotation.test5"},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = MyFilter.class)
        }
)
public class ScanBean6 {
}
