package us.fjj.spring.learning.annotationimplementDI.other.test4.dao;

import org.springframework.stereotype.Component;
import us.fjj.spring.learning.annotationimplementDI.other.test4.model.OrderModel;

@Component
public class OrderDao implements IDao<OrderModel> {
}
