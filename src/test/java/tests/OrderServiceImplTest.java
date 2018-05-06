package tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import models.Client;
import models.Item;
import models.Order;
import models.OrderItem;
import services.OrderServiceImpl;
import storages.OrderItemStorage;
import storages.OrderStorage;

public class OrderServiceImplTest {

	Order order;
	List<Item> items = new ArrayList<Item>();
	
	@Mock
	OrderStorage orderStorage;
	
	@Mock
	OrderItemStorage orderItemStorage;

	@InjectMocks
	OrderServiceImpl orderService;
	
	@Captor
    ArgumentCaptor<Order> captor;
	
	@Spy
    List<Order> orders = new ArrayList<Order>();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		captor = ArgumentCaptor.forClass(Order.class);
		orders = getOrdersList();
		for(int i=0; i<2; i++)
			items.add(new Item());
	}
	
	@Test
	public void addWithOrderArgAddsOrder() {
		doNothing().when(orderStorage).add(any(Order.class));
		orderService.add(orders.get(0));
		
		verify(orderStorage, times(1)).findById(orders.get(0).getId());
		verify(orderStorage, times(1)).add(captor.capture());
		
		assertEquals(captor.getValue(), orders.get(0));
	}
	
	@Test
	public void addWithOrderArgAddsOrdersAndOrderItems() {
		Order order = orders.get(1);
		doNothing().when(orderStorage).add(any(Order.class));
		orderService.add(order);
		
		verify(orderStorage, times(1)).findById(order.getId());
		verify(orderStorage, times(1)).add(captor.capture());
		verify(orderItemStorage, times(1)).add(order.getOrderItems().get(0));
		verify(orderItemStorage, times(1)).add(order.getOrderItems().get(1));
		
		assertEquals(order, captor.getValue());
	}
	
	@Test
	public void addWithOrderArgAddsOrdersAndOrderItemsWhichDontExist() {
		Order order = orders.get(1);
		doNothing().when(orderStorage).add(any(Order.class));
		doReturn(order.getOrderItems().get(0)).when(orderItemStorage).findById(2);
		orderService.add(order);
		
		verify(orderStorage, times(1)).findById(order.getId());
		verify(orderStorage, times(1)).add(captor.capture());
		verify(orderItemStorage, times(1)).add(order.getOrderItems().get(0));
		verify(orderItemStorage, times(0)).add(order.getOrderItems().get(1));
		
		assertEquals(order, captor.getValue());
	}
	
	@Test
	public void addWithExistingOrderThrowsException() {
		doReturn(orders.get(0)).when(orderStorage).findById(1);
		
		Throwable thrown = catchThrowable(() -> { orderService.add(orders.get(0)); });
		assertThat(thrown).hasMessageContaining("Order exists!");
	}
	
	@Test
	public void addWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { orderService.add(null); });
		assertThat(thrown).hasMessageContaining("Order cannot be null!");
	}
	
	@Test
	public void updateWithOrderArgUpdatesOrder() {
		doReturn(orders.get(0)).when(orderStorage).findById(1);
		doNothing().when(orderStorage).update(any(Order.class));
		
		orderService.update(orders.get(0));
		
		verify(orderStorage, times(1)).findById(orders.get(0).getId());
		verify(orderStorage, times(1)).update(captor.capture());

		assertEquals(captor.getValue(), orders.get(0));
	}
	
	@Test
	public void updateWithOrderArgUpdatesOrderAndOrderItems() {
		Order order = orders.get(1);
		doReturn(order).when(orderStorage).findById(2);
		doNothing().when(orderStorage).update(any(Order.class));
		
		orderService.update(order);
		
		verify(orderStorage, times(1)).findById(order.getId());
		verify(orderStorage, times(1)).update(captor.capture());
		verify(orderItemStorage, times(1)).add(order.getOrderItems().get(0));
		verify(orderItemStorage, times(1)).add(order.getOrderItems().get(1));

		assertEquals(order, captor.getValue());
	}
	
	@Test
	public void updateWithOrderArgUpdatesOrderAndOrderItemsWhichDontExist() {
		Order order = orders.get(1);
		doReturn(order).when(orderStorage).findById(2);
		doReturn(order.getOrderItems().get(1)).when(orderItemStorage).findById(2);
		doNothing().when(orderStorage).update(any(Order.class));
		
		orderService.update(order);
		
		verify(orderStorage, times(1)).findById(order.getId());
		verify(orderStorage, times(1)).update(captor.capture());
		verify(orderItemStorage, times(1)).add(order.getOrderItems().get(0));
		verify(orderItemStorage, times(0)).add(order.getOrderItems().get(1));

		assertEquals(order, captor.getValue());
	}
	
	@Test
	public void updateWithOrderArgUpdatesOrderAndRemoveOldOrderItems() {
		Order order = orders.get(1);
		
		List<OrderItem> oldOrderItems = new ArrayList<OrderItem>();
		oldOrderItems.add(order.getOrderItems().get(1));
		
		order.getOrderItems().remove(1);
				
		doNothing().when(orderStorage).update(any(Order.class));
		doReturn(oldOrderItems).when(orderStorage).getOrderItems(order);
		doReturn(order).when(orderStorage).findById(2);
		
		orderService.update(order);
		
		verify(orderStorage, times(1)).findById(order.getId());
		verify(orderStorage, times(1)).update(captor.capture());
		verify(orderItemStorage, times(1)).add(order.getOrderItems().get(0));
		verify(orderItemStorage, times(1)).delete(oldOrderItems.get(0));

		assertEquals(order, captor.getValue());
	}
	
	@Test
	public void updateWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { orderService.update(null); });
		assertThat(thrown).hasMessageContaining("Order cannot be null!");
	}
	
	@Test
	public void updateWhenOrderDoesntExistThrowsException() {
		doNothing().when(orderStorage).update(any(Order.class));
		
		Throwable thrown = catchThrowable(() -> { orderService.update(orders.get(0)); });
		
		verify(orderStorage, times(1)).findById(orders.get(0).getId());
		
		assertThat(thrown).hasMessageContaining("Order does not exist!");
	}
	
	@Test
	public void deleteWithOrderArgDeletesOrder() {
		doReturn(orders.get(0)).when(orderStorage).findById(1);
		doNothing().when(orderStorage).delete(any(Order.class));
		
		orderService.delete(orders.get(0));
		
		verify(orderStorage, times(1)).findById(orders.get(0).getId());
		verify(orderStorage, times(1)).delete(captor.capture());

		assertEquals(captor.getValue(), orders.get(0));
	}
	
	@Test
	public void deleteWithOrderArgDeletesOrderAndOrderItems() {
		Order order = orders.get(1);
		doReturn(order).when(orderStorage).findById(2);
		doReturn(order.getOrderItems().get(0)).when(orderItemStorage).findById(1);
		doReturn(order.getOrderItems().get(1)).when(orderItemStorage).findById(2);
		doNothing().when(orderStorage).delete(any(Order.class));
		
		orderService.delete(order);
		
		verify(orderStorage, times(1)).findById(order.getId());
		verify(orderStorage, times(1)).delete(captor.capture());
		verify(orderItemStorage, times(1)).delete(order.getOrderItems().get(0));
		verify(orderItemStorage, times(1)).delete(order.getOrderItems().get(1));

		assertEquals(order, captor.getValue());
	}
	
	@Test
	public void deleteWithOrderArgDeletesOrderAndOrderItemsWhichExists() {
		Order order = orders.get(1);
		doReturn(order).when(orderStorage).findById(2);
		doReturn(order.getOrderItems().get(0)).when(orderItemStorage).findById(1);
		doNothing().when(orderStorage).delete(any(Order.class));
		
		orderService.delete(orders.get(1));
		
		verify(orderStorage, times(1)).findById(order.getId());
		verify(orderStorage, times(1)).delete(captor.capture());
		verify(orderItemStorage, times(1)).delete(order.getOrderItems().get(0));
		verify(orderItemStorage, times(0)).delete(order.getOrderItems().get(1));
		
		assertEquals(order, captor.getValue());
	}
	
	@Test
	public void deleteWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { orderService.delete(null); });
		assertThat(thrown).hasMessageContaining("Order cannot be null!");
	}
	
	@Test
	public void deleteWhenOrderDoesntExistThrowsException() {
		doNothing().when(orderStorage).delete(any(Order.class));
		
		Throwable thrown = catchThrowable(() -> { orderService.delete(orders.get(0)); });
		
		verify(orderStorage, times(1)).findById(orders.get(0).getId());
		
		assertThat(thrown).hasMessageContaining("Order does not exist!");
	}
	
	@Test
	public void getAllOrdersReturnsOrders() {
		doReturn(orders).when(orderStorage).getAllOrders();
		
		List<Order> result = orderService.getAllOrders();
		
		verify(orderStorage, times(1)).getAllOrders();
		
		assertEquals(orders, result);
	}
	
	@Test
	public void getItemsWithOrderArgReturnsItems() {
		doReturn(orders.get(0)).when(orderStorage).findById(1);
		doReturn(items).when(orderStorage).getItems(any(Order.class));
		
		List<Item> result = orderService.getItems(orders.get(0));
		
		verify(orderStorage, times(1)).getItems(orders.get(0));
		
		assertEquals(items, result);
	}
	
	@Test
	public void getItemsWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { orderService.getItems(null); });
		assertThat(thrown).hasMessageContaining("Order cannot be null!");
	}
	
	@Test
	public void getItemsWhenOrderDoesntExistThrowsException() {
		doReturn(items).when(orderStorage).getItems(any(Order.class));
		
		Throwable thrown = catchThrowable(() -> { orderService.getItems(orders.get(0)); });
		
		verify(orderStorage, times(1)).findById(orders.get(0).getId());
		
		assertThat(thrown).hasMessageContaining("Order does not exist!");
	}
	
	@Test
	public void findByIdReturnsOrder() {
		doReturn(orders.get(0)).when(orderStorage).findById(1);
		
		Order result = orderService.findById(orders.get(0).getId());
		
		verify(orderStorage, times(1)).findById(orders.get(0).getId());
		
		assertEquals(orders.get(0), result);
	}
	
	@Test
	public void findByIdWithArgNullThrowsException() {
		Throwable thrown = catchThrowable(() -> { orderService.findById(null); });
		assertThat(thrown).hasMessageContaining("ID cannot be null!");
	}
	
	@Test
	public void orderExistsWithArgNullThrowsException() {
		Throwable thrown = catchThrowable(() -> { orderService.orderExists(null); });
		assertThat(thrown).hasMessageContaining("Order cannot be null!");
	}
	
	@Test
	public void orderExistsWhenOrderExistReturnsTrue() {
		doReturn(orders.get(0)).when(orderStorage).findById(1);
		
		boolean result = orderService.orderExists(orders.get(0));
		
		verify(orderStorage, times(1)).findById(orders.get(0).getId());
		
		assertTrue(result);
	}
	
	@Test
	public void clientExistsWhenClientDoesntExistReturnsFalse() {
		boolean result = orderService.orderExists(orders.get(0));
		
		verify(orderStorage, times(1)).findById(orders.get(0).getId());
		
		assertFalse(result);
	}
	
	@After
	public void clean() {
		captor = null;
		orders = null;
		items = null;
	}
	
    public List<Order> getOrdersList() { 
    		Client client = new Client(1, "Pawel", "Krauze", "pkrauze@test.com");
    		Order order1 = new Order(1, client);
    		Order order2 = new Order(2, client);
    		    		
    		List<OrderItem> orderItems = new ArrayList<OrderItem>();
    		orderItems.add(new OrderItem(1, order2, new Item()));
    		orderItems.add(new OrderItem(2, order2, new Item()));
    		
    		order2.setOrderItems(orderItems);
    		
        orders.add(order1);
        orders.add(order2);
        return orders;
    }
}
