package mocks;

import java.util.ArrayList;
import java.util.List;

import models.Item;
import models.Order;
import models.OrderItem;
import storages.ItemStorage;

public class MockItemStorage implements ItemStorage {
	private List<Item> items = new ArrayList<Item>();
	
	@Override
	public void add(Item item) {
		items.add(item);
	}

	@Override
	public Item findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Item> getAllItems() {
		return items;
	}

	@Override
	public List<Order> getOrders(Item item) {
		List<Order> orders = new ArrayList<Order>();
		for(OrderItem orderItem : item.getOrderItems()) {
			orders.add(orderItem.getOrder());
		}
		return orders;
	}

	@Override
	public void update(Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Item item) {
		items.remove(item);
	}
	
	@Override
	public List<OrderItem> getOrderItems(Item item) {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		Item find = items.stream().filter(i -> i.getId() == item.getId()).findAny().orElse(null);
		if(find == null)
			return orderItems;
		else
			return find.getOrderItems();
	}

}
