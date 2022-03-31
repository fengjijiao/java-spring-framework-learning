package us.fjj.spring.learning.databaseseparateusage.base;

/**
 * 这个接口起到标志的作用，当某个类需要启用读写分离的时候，需要实现这个接口，实现这个接口的类都会被读写分离拦截器拦截。
 */
public interface IService {
}
