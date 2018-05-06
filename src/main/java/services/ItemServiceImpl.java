package services;

import java.util.ArrayList;
import java.util.List;

import models.Item;
import models.Order;
import models.OrderItem;
import storages.ItemStorage;
import storages.OrderItemStorage;

public class ItemServiceImpl implements ItemService {
	private ItemStorage itemStorage;
	private OrderItemStorage orderItemStorage;
	
	public ItemServiceImpl() {
		
	}
	
	public ItemServiceImpl(ItemStorage itemStorage, OrderItemStorage orderItemStorage) {
		this.itemStorage = itemStorage;
		this.orderItemStorage = orderItemStorage;
	}

	public static ItemServiceImpl createWith(final ItemStorage itemStorage, final OrderItemStorage orderItemStorage) {
		return new ItemServiceImpl(itemStorage, orderItemStorage);
	}
	
	@Override
	public void add(Item item) {
		if(item == null)
			throw new IllegalArgumentException("Item cannot be null!");
		if(itemExists(item))
			throw new IllegalArgumentException("Item exists!");
		
		for(OrderItem orderItem : item.getOrderItems()) {
			if(orderItemStorage.findById(orderItem.getId()) == null)
				orderItemStorage.add(orderItem);
		}
		itemStorage.add(item);
	}
	
	@Override
	public void update(Item item) {
		if(item == null)
			throw new IllegalArgumentException("Item cannot be null!");
		if(!itemExists(item))
			throw new IllegalArgumentException("Item does not exist!");
		
		
		for(OrderItem orderItem : item.getOrderItems()) {
			if(orderItemStorage.findById(orderItem.getId()) == null)
				orderItemStorage.add(orderItem);
		}
		
		if(itemStorage.getOrderItems(item) != null) {
			// Remove old OrderItems which are no longer associated
			List<OrderItem> toRemove = new ArrayList<OrderItem>(itemStorage.getOrderItems(item));
			toRemove.removeAll(item.getOrderItems());
			
			for(OrderItem orderItem : toRemove) {
				orderItemStorage.delete(orderItem);
			}
		}
		
		itemStorage.update(item);
	}

	@Override
	public void delete(Item item) {
		if(item == null)
			throw new IllegalArgumentException("Item cannot be null!");
		if(!itemExists(item))
			throw new IllegalArgumentException("Item does not exist!");
		
		for(OrderItem orderItem : item.getOrderItems()) {
			if(orderItemStorage.findById(orderItem.getId()) != null)
				orderItemStorage.delete(orderItem);
		}
		itemStorage.delete(item);
	}

	@Override
	public Item findById(Long id) {
		if(id == null)
			throw new IllegalArgumentException("ID cannot be null!");
		
		return itemStorage.findById(id);
	}

	@Override
	public List<Item> getAllItems() {
		return itemStorage.getAllItems();
	}

	@Override
	public List<Order> getItemOrders(Item item) {
		if(item == null)
			throw new IllegalArgumentException("Item cannot be null!");
		if(!itemExists(item))
			throw new IllegalArgumentException("Item does not exist!");
		
		return itemStorage.getOrders(item);
	}

	@Override
	public boolean itemExists(Item item) {
		if(item == null)
			throw new IllegalArgumentException("Item cannot be null!");
		if(findById(item.getId()) != null)
			return true;
		return false;
	}
}
