package storages;

import java.util.List;

import models.Item;
import models.Order;
import models.OrderItem;

public interface OrderStorage {
	void add(Order order);
    Order findById(long id);
	List<Order> getAllOrders();
	List<Item> getItems(Order order);
	List<OrderItem> getOrderItems(Order order);
	void update(Order order);
	void delete(Order order);
}
