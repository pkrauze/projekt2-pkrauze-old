package mocks;

import java.util.ArrayList;
import java.util.List;

import models.OrderItem;
import storages.OrderItemStorage;

public class MockOrderItemStorage implements OrderItemStorage {

	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	@Override
	public void add(OrderItem orderItem) {
		orderItems.add(orderItem);
	}

	@Override
	public void delete(OrderItem orderItem) {
		orderItems.remove(orderItem);
	}
	
	@Override
	public List<OrderItem> getAllOrderItems() {
		return orderItems;
	}

	@Override
	public void update(OrderItem orderItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OrderItem findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
