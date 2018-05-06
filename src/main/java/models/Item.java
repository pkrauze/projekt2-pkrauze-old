package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Item")
public class Item {
	private long id;
	private String name;
	private double value;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	public Item() {
		super();
	}
	
	public Item(long id, String name, double value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}
	
	public Item(long id, String name, double value, List<OrderItem> orderItems) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.orderItems = orderItems;
	}
	
	public void addItem(OrderItem orderItem) {
		orderItems.add(orderItem);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}
