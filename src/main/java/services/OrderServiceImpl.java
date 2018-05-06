package services;

import java.util.ArrayList;
import java.util.List;

import models.Item;
import models.Order;
import models.OrderItem;
import storages.OrderItemStorage;
import storages.OrderStorage;

public class OrderServiceImpl implements OrderService {
	private final OrderStorage orderStorage;
	private final OrderItemStorage orderItemStorage;
	
	public OrderServiceImpl(OrderStorage orderStorage, OrderItemStorage orderItemStorage) {
		this.orderStorage = orderStorage;
		this.orderItemStorage = orderItemStorage;
	}

	public static OrderServiceImpl createWith(final OrderStorage orderStorage, OrderItemStorage orderItemStorage) {
		return new OrderServiceImpl(orderStorage, orderItemStorage);
	}
	
	@Override
	public void add(Order order) {
		if(order == null)
			throw new IllegalArgumentException("Order cannot be null!");
		if(orderExists(order))
			throw new IllegalArgumentException("Order exists!");
		
		for(OrderItem orderItem : order.getOrderItems()) {
			if(orderItemStorage.findById(orderItem.getId()) == null)
				orderItemStorage.add(orderItem);
		}
		orderStorage.add(order);
	}
	
	@Override
	public void update(Order order) {
		if(order == null)
			throw new IllegalArgumentException("Order cannot be null!");
		if(!orderExists(order))
			throw new IllegalArgumentException("Order does not exist!");
		
		for(OrderItem orderItem : order.getOrderItems()) {
			if(orderItemStorage.findById(orderItem.getId()) == null)
				orderItemStorage.add(orderItem);
		}
		
		// Remove old OrderItems which are no longer associated
		List<OrderItem> toRemove = new ArrayList<OrderItem>(orderStorage.getOrderItems(order));
		toRemove.removeAll(order.getOrderItems());
		
		for(OrderItem orderItem : toRemove) {
			orderItemStorage.delete(orderItem);
		}
		
		orderStorage.update(order);
	}

	@Override
	public void delete(Order order) {
		if(order == null)
			throw new IllegalArgumentException("Order cannot be null!");
		if(!orderExists(order))
			throw new IllegalArgumentException("Order does not exist!");
		
		for(OrderItem orderItem : order.getOrderItems()) {
			if(orderItemStorage.findById(orderItem.getId()) != null)
				orderItemStorage.delete(orderItem);
		}
		orderStorage.delete(order);
	}

	@Override
	public Order findById(Long id) {
		if(id == null)
			throw new IllegalArgumentException("ID cannot be null!");
		
		return orderStorage.findById(id);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderStorage.getAllOrders();
	}
	
	@Override
	public List<Item> getItems(Order order) {
		if(order == null)
			throw new IllegalArgumentException("Order cannot be null!");
		if(!orderExists(order))
			throw new IllegalArgumentException("Order does not exist!");
		
		return orderStorage.getItems(order);
	}

	@Override
	public boolean orderExists(Order order) {
		if(order == null)
			throw new IllegalArgumentException("Order cannot be null!");
		
		if(findById(order.getId()) != null)
			return true;
		return false;
	}
}
