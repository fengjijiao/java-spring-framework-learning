package us.fjj.spring.learning.annotationimplementDI.other.test4.dao;

import org.springframework.stereotype.Component;
import us.fjj.spring.learning.annotationimplementDI.other.test4.model.UserModel;

@Component
public class UserDao implements IDao<UserModel> {
}
