package us.fjj.spring.learning.aspectpointcutusage.test15;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

public class AspectPcDefine15 {
    @Pointcut("bean(bean1)")
    public void pc1() {}
    @Pointcut("bean(bean2)")
    public void pc2() {}
}
