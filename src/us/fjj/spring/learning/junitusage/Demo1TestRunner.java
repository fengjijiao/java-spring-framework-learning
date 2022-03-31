package us.fjj.spring.learning.junitusage;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class Demo1TestRunner {
    public static void main(String[] args) {
        //设置搜索和过滤规则
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
//                        selectPackage("test"),
                        selectClass(MathUtilsTest.class)
                )
//                .filters(
//                        includeClassNamePatterns(".*Tests")
//                )
                .build();
        Launcher launcher = LauncherFactory.create();
        //Register a listener of your choice
        //通过监听器来监听获取执行结果
        TestExecutionListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        /**
         * max() tested
         * min() tested
         */
    }
}
