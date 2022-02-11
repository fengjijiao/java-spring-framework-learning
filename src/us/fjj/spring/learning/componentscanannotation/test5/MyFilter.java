package us.fjj.spring.learning.componentscanannotation.test5;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class MyFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        Class curClass = null;
        try {
            //当前被扫描的类
            curClass = Class.forName(metadataReader.getClassMetadata().getClassName());
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        //判断curClass是否是IService类型
        boolean result = IService.class.isAssignableFrom(curClass);
        return result;
    }
}
