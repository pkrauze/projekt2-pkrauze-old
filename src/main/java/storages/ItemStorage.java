package storages;

import java.util.List;

import models.Item;
import models.Order;
import models.OrderItem;

public interface ItemStorage {
	void add(Item item);
	Item findById(long id);
	List<Item> getAllItems();
	List<Order> getOrders(Item item);
	List<OrderItem> getOrderItems(Item item);
	void update(Item item);
	void delete(Item item);
}
