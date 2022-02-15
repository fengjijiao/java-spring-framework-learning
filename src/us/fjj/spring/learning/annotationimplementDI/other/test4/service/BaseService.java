package us.fjj.spring.learning.annotationimplementDI.other.test4.service;

import org.springframework.beans.factory.annotation.Autowired;
import us.fjj.spring.learning.annotationimplementDI.other.test4.dao.IDao;

public class BaseService<T> {
    @Autowired
    private IDao<T> dao;

    public IDao<T> getDao() {
        return dao;
    }

    public void setDao(IDao<T> dao) {
        this.dao = dao;
    }
}
