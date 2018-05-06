package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Order")
public class Order {
	private long id;
	private Client client;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	public Order() {
		super();
	}
	
	public Order(long id, Client client) {
		super();
		this.id = id;
		this.client = client;
	}

	public Order(long id, Client client, List<OrderItem> items) {
		super();
		this.id = id;
		this.client = client;
		this.orderItems = items;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public void addItem(OrderItem orderItem) {
		orderItems.add(orderItem);
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public void setClient(Client client) {
		this.client = client;
	}
	
	public Client getClient() {
		return client;
	}
}
