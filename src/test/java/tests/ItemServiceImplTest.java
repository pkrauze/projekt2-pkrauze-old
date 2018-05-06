package tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import static org.easymock.EasyMock.*;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.Item;
import models.Order;
import models.OrderItem;
import services.ItemServiceImpl;
import storages.ItemStorage;
import storages.OrderItemStorage;

public class ItemServiceImplTest {
	
	@Mock(type = MockType.NICE)
	ItemStorage itemStorage;
	
	@Mock(type = MockType.NICE)
	OrderItemStorage orderItemStorage;
	
	@TestSubject
	ItemServiceImpl itemService = new ItemServiceImpl();
	
	List<Item> items = new ArrayList<Item>();
	List<Order> orders = new ArrayList<Order>();
	
	@Before
	public void setUp() {
		itemStorage = createNiceMock(ItemStorage.class);
		orderItemStorage = createNiceMock(OrderItemStorage.class);
		itemService = ItemServiceImpl.createWith(itemStorage, orderItemStorage);
		items = getItemsList();
		for(int i=0; i<2; i++)
			orders.add(new Order());
	}
	
	@Test
	public void addWithItemArgAddsItem() {		
		expect(itemStorage.findById(1)).andReturn(items.get(0)).times(1);
		replay(itemStorage);
		
		assertEquals(items.get(0), itemService.findById((long) 1));
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void addWithItemArgAddsItemsAndOrderItems() {
		Item item = items.get(1);
		expect(itemStorage.findById(item.getId())).andReturn(null).times(1);
		expect(orderItemStorage.findById(1)).andReturn(null).times(1);
		expect(orderItemStorage.findById(2)).andReturn(null).times(1);
		replay(itemStorage);
		replay(orderItemStorage);
		
		itemService.add(item);
		EasyMock.verify(itemStorage);
		EasyMock.verify(orderItemStorage);
	}
	
	@Test
	public void addWithItemArgAddsItemsAndOrderItemsWhichDontExist() {
		Item item = items.get(1);
		expect(itemStorage.findById(item.getId())).andReturn(null).times(1);
		expect(orderItemStorage.findById(1)).andReturn(null).times(1);
		expect(orderItemStorage.findById(2)).andReturn(item.getOrderItems().get(1)).times(1);
		replay(itemStorage);
		replay(orderItemStorage);
		
		itemService.add(item);
		EasyMock.verify(itemStorage);
		EasyMock.verify(orderItemStorage);
	}
	
	@Test
	public void addWithExistingOrderThrowsException() {
		expect(itemStorage.findById(items.get(0).getId())).andReturn(items.get(0)).times(1);
		replay(itemStorage);
		
		Throwable thrown = catchThrowable(() -> { itemService.add(items.get(0)); });
		assertThat(thrown).hasMessageContaining("Item exists!");
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void addWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { itemService.add(null); });
		assertThat(thrown).hasMessageContaining("Item cannot be null!");
	}
	
	@Test
	public void updateWithItemArgUpdatesItem() {
		expect(itemStorage.findById(items.get(0).getId())).andReturn(items.get(0)).times(1);
		replay(itemStorage);
		
		itemService.update(items.get(0));
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void updateWithItemArgUpdatesItemAndOrderItems() {
		Item item = items.get(1);
		expect(itemStorage.findById(item.getId())).andReturn(item);
		expect(itemStorage.getOrderItems(item)).andReturn(item.getOrderItems()).times(2);
		expect(orderItemStorage.findById(1)).andReturn(null);
		expect(orderItemStorage.findById(2)).andReturn(null);
		replay(itemStorage);
		replay(orderItemStorage);
		
		itemService.update(item);
		
		EasyMock.verify(itemStorage);
		EasyMock.verify(orderItemStorage);
	}
	
	@Test
	public void updateWithItemArgUpdatesItemAndOrderItemsWhichDontExist() {
		Item item = items.get(1);
		item.getOrderItems().remove(1);
		
		expect(itemStorage.findById(item.getId())).andReturn(item);
		expect(itemStorage.getOrderItems(item)).andReturn(item.getOrderItems()).times(2);
		expect(orderItemStorage.findById(1)).andReturn(item.getOrderItems().get(0));
		
		replay(itemStorage);
		replay(orderItemStorage);
		
		itemService.update(item);
		
		EasyMock.verify(itemStorage);
		EasyMock.verify(orderItemStorage);
	}

	@Test
	public void updateWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { itemService.update(null); });
		assertThat(thrown).hasMessageContaining("Item cannot be null!");
	}
	
	@Test
	public void updateWhenItemDoesntExistThrowsException() {
		expect(itemStorage.findById(1)).andReturn(null);
		replay(itemStorage);
		
		Throwable thrown = catchThrowable(() -> { itemService.update(items.get(0)); });
		assertThat(thrown).hasMessageContaining("Item does not exist!");
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void deleteWithItemArgDeletesItem() {
		expect(itemStorage.findById(items.get(0).getId())).andReturn(items.get(0)).times(1);
		replay(itemStorage);
		
		itemService.delete(items.get(0));
		
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void deleteWithItemArgDeletesItemAndOrderItems() {
		Item item = items.get(1);
		expect(itemStorage.findById(2)).andReturn(item).times(1);
		expect(orderItemStorage.findById(1)).andReturn(item.getOrderItems().get(0)).times(1);
		expect(orderItemStorage.findById(2)).andReturn(item.getOrderItems().get(1)).times(1);
		replay(itemStorage);
		replay(orderItemStorage);
		
		itemService.delete(item);
		
		EasyMock.verify(itemStorage);
		EasyMock.verify(orderItemStorage);
	}
	
	@Test
	public void deleteWithItemArgDeletesItemAndOrderItemsWhichExists() {
		Item item = items.get(1);
		expect(itemStorage.findById(2)).andReturn(item).times(1);
		expect(orderItemStorage.findById(1)).andReturn(item.getOrderItems().get(0)).times(1);
		expect(orderItemStorage.findById(2)).andReturn(null).times(1);
		replay(itemStorage);
		replay(orderItemStorage);
		
		itemService.delete(item);
		
		EasyMock.verify(itemStorage);
		EasyMock.verify(orderItemStorage);
	}
	
	@Test
	public void deleteWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { itemService.delete(null); });
		assertThat(thrown).hasMessageContaining("Item cannot be null!");
	}
	
	@Test
	public void deleteWhenItemDoesntExistThrowsException() {
		expect(itemStorage.findById(1)).andReturn(null).times(1);
		replay(itemStorage);
		
		Throwable thrown = catchThrowable(() -> { itemService.delete(items.get(0)); });
		assertThat(thrown).hasMessageContaining("Item does not exist!");
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void getAllOrdersReturnsOrders() {
		expect(itemStorage.getAllItems()).andReturn(items).times(1);
		replay(itemStorage);
		
		List<Item> result = itemService.getAllItems();
		
		assertEquals(items, result);
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void getOrdersWithItemArgReturnsItems() {
		expect(itemStorage.findById(2)).andReturn(items.get(1)).times(1);
		expect(itemStorage.getOrders(items.get(1))).andReturn(orders).times(1);
		replay(itemStorage);
		
		List<Order> result = itemService.getItemOrders(items.get(1));
		
		assertEquals(orders, result);
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void getOrdersWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { itemService.getItemOrders(null); });
		assertThat(thrown).hasMessageContaining("Item cannot be null!");
	}
	
	@Test
	public void getOrdersWhenItemDoesntExistThrowsException() {
		expect(itemStorage.findById(1)).andReturn(null).times(1);
		replay(itemStorage);
		
		Throwable thrown = catchThrowable(() -> { itemService.getItemOrders(items.get(0)); });
		assertThat(thrown).hasMessageContaining("Item does not exist!");
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void findByIdReturnsItem() {
		expect(itemStorage.findById(1)).andReturn(items.get(0)).times(1);
		replay(itemStorage);
		
		Item result = itemService.findById((long) 1);
		
		assertEquals(items.get(0), result);
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void findByIdWithArgNullThrowsException() {
		Throwable thrown = catchThrowable(() -> { itemService.findById(null); });
		assertThat(thrown).hasMessageContaining("ID cannot be null!");
	}
	
	@Test
	public void itemExistsWithArgNullThrowsException() {
		Throwable thrown = catchThrowable(() -> { itemService.itemExists(null); });
		assertThat(thrown).hasMessageContaining("Item cannot be null!");
	}
	
	@Test
	public void itemExistsWhenItemExistReturnsTrue() {
		expect(itemStorage.findById(1)).andReturn(items.get(0)).times(1);
		replay(itemStorage);
		
		boolean result = itemService.itemExists(items.get(0));
		
		assertTrue(result);
		EasyMock.verify(itemStorage);
	}
	
	@Test
	public void clientExistsWhenClientDoesntExistReturnsFalse() {
		expect(itemStorage.findById(1)).andReturn(null).times(1);
		replay(itemStorage);
		
		boolean result = itemService.itemExists(items.get(0));
		
		assertFalse(result);
		EasyMock.verify(itemStorage);
	}
	
	@After
	public void clean() {
		itemService = null;
		items = null;
	}
	
    public List<Item> getItemsList() { 
    		Item item1 = new Item(1, "Test1", 20);
    		Item item2 = new Item(2, "Test1", 20);
		    		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add(new OrderItem(1, new Order(), item1));
		orderItems.add(new OrderItem(2, new Order(), item2));
		
		item2.setOrderItems(orderItems);
		
	    items.add(item1);
	    items.add(item2);
	    return items;
	}
}
