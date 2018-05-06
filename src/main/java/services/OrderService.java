package services;

import java.util.List;

import models.Item;
import models.Order;

public interface OrderService {
	void add(Order order);
	void update(Order order);
	void delete(Order order);
    Order findById(Long id);
	List<Order> getAllOrders();
	List<Item> getItems(Order order);
	boolean orderExists(Order order);
}
