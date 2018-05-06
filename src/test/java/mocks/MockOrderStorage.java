package mocks;

import java.util.ArrayList;
import java.util.List;

import models.Item;
import models.Order;
import models.OrderItem;
import storages.OrderStorage;

public class MockOrderStorage implements OrderStorage {
	private List<Order> orders = new ArrayList<Order>();
	
	@Override
	public void add(Order order) {
		orders.add(order);

	}

	@Override
	public Order findById(long id) {
		return orders.stream().filter(o -> o.getId() == id).findAny().orElse(null);
	}

	@Override
	public List<Order> getAllOrders() {
		return orders;
	}

	@Override
	public void update(Order order) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Order order) {
		orders.remove(order);

	}

	@Override
	public List<Item> getItems(Order order) {		
		List<Item> items = new ArrayList<Item>();
		for(OrderItem orderItem : order.getOrderItems()) {
			items.add(orderItem.getItem());
		}
		return items;
	}

	@Override
	public List<OrderItem> getOrderItems(Order order) {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		Order find = orders.stream().filter(o -> o.getId() == order.getId()).findAny().orElse(null);
		if(find == null)
			return orderItems;
		else
			return find.getOrderItems();
	}

}
