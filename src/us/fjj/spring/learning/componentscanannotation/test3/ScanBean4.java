package us.fjj.spring.learning.componentscanannotation.test3;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyBean.class)
        },
        useDefaultFilters = false//不启用默认过滤器
)
public class ScanBean4 {
}
