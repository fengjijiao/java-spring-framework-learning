package us.fjj.spring.learning.proxyexpend.cglib.test4;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.LazyLoader;

//博客信息
public class BlogModel {
    private String title;
    //博客内容信息比较多，需要的时候再去获取
    private BlogContentModel blogContentModel;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBlogContentModel(BlogContentModel blogContentModel) {
        this.blogContentModel = blogContentModel;
    }

    public BlogContentModel getBlogContentModel() {
        return blogContentModel;
    }

    public BlogModel() {
        this.title = "spring aop详解！";
        this.blogContentModel = this.getBlogContentModelLazyLoader();
    }

    private BlogContentModel getBlogContentModelLazyLoader() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BlogContentModel.class);
        enhancer.setCallback(new LazyLoader() {
            @Override
            public Object loadObject() throws Exception {
                //此处模拟从数据库中获取博客内容
                System.out.println("开始从数据库中获取博客内容...");
                BlogContentModel result = new BlogContentModel();
                result.setContent("lxh yyds!");
                return result;
            }
        });
        return (BlogContentModel) enhancer.create();
    }
}
