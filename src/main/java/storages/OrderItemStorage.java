package storages;

import java.util.List;

import models.OrderItem;

public interface OrderItemStorage {
	void add(OrderItem orderItem);
	void delete(OrderItem orderItem);
	void update(OrderItem orderItem);
	List<OrderItem> getAllOrderItems();
	OrderItem findById(long id);
}
