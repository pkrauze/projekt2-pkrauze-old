package tests;

import static org.assertj.core.api.Assertions.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mocks.MockClientStorage;
import mocks.MockOrderItemStorage;
import mocks.MockOrderStorage;
import models.Client;
import models.Order;
import services.ClientService;
import services.ClientServiceImpl;
import services.OrderService;
import services.OrderServiceImpl;
import storages.ClientStorage;
import storages.OrderItemStorage;
import storages.OrderStorage;

public class ClientServiceImplTest  {
	ClientService clientService;
	ClientStorage clientStorage;
	OrderService orderService;
	OrderStorage orderStorage;
	OrderItemStorage orderItemStorage;
	Client client;
	
	@Before
    public void setUp() {
	    clientStorage = new MockClientStorage();
	    orderStorage = new MockOrderStorage();
	    orderItemStorage = new MockOrderItemStorage();
	    orderService = OrderServiceImpl.createWith(orderStorage, orderItemStorage);
	    clientService = ClientServiceImpl.createWith(clientStorage, orderService);
	    client = new Client(1, "Pawel", "Krauze", "pkrauze@test.com");
    }
	
	@Test
	public void addWithClientArgAddsClient() {		
		clientService.add(client);
	
		Client result = clientStorage.findById(1);
		
		assertEquals(client, result);
	}
	
	@Test
	public void addWithExistingClientThrowsException() {
		clientService.add(client);
		
		//Second time
		Throwable thrown = catchThrowable(() -> { clientService.add(client); });
		assertThat(thrown).hasMessageContaining("Client exists!");
	}
	
	@Test
	public void addWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { clientService.add(null); });
		assertThat(thrown).hasMessageContaining("Client cannot be null!");
	}
	
	@Test
	public void updateWithClientArgUpdatesClient() {
		clientStorage.add(client);
		client.setFirstname("Blazej");
		
		clientService.update(client);
		
		String expected = "Blazej";
		String result = clientStorage.findById(1).getFirstname();
		
		assertEquals(expected, result);
	}
	
	@Test
	public void updateWithNullArgThrowsException() {
		clientStorage.add(client);
		
		Throwable thrown = catchThrowable(() -> { clientService.update(null); });
		assertThat(thrown).hasMessageContaining("Client cannot be null!");
	}
	
	@Test
	public void updateWhenClientDoesntExistThrowsException() {
		Throwable thrown = catchThrowable(() -> { clientService.update(client); });
		assertThat(thrown).hasMessageContaining("Client does not exist!");
	}
	
	@Test
	public void deleteWithClientArgDeletesClient() {
		clientStorage.add(client);
		
		clientService.delete(client);
		
		boolean result = clientStorage.getAllClients().isEmpty();
		
		assertTrue(result);
	}
	
	@Test
	public void deleteWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { clientService.delete(null); });
		assertThat(thrown).hasMessageContaining("Client cannot be null!");
	}
	
	@Test
	public void deleteWhenClientDoesntExistThrowsException() {
		Throwable thrown = catchThrowable(() -> { clientService.delete(client); });
		assertThat(thrown).hasMessageContaining("Client does not exist!");
	}
	
	@Test
	public void deleteRemovesAssociatedOrders() {
		Order order = new Order();
		client.addOrder(order);
		clientStorage.add(client);
		orderStorage.add(order);
		
		clientService.delete(client);
		
		List<Order> result = orderStorage.getAllOrders();
		
		assertThat(result).hasSize(0);
	}
	
	@Test
	public void getAllClientsReturnsClients() {
		clientStorage.add(client);
		
		List<Client> result = clientService.getAllClients();
		
		assertThat(result).hasSize(1);
	}
	
	@Test
	public void getClientOrdersWithClientArgReturnsOrders() {
		Order order = new Order();
		client.addOrder(order);
		clientStorage.add(client);
		orderStorage.add(order);
		
		List<Order> result = clientService.getClientOrders(client);
		
		assertThat(result).hasSize(1);
	}
	
	@Test
	public void getClientOrdersWithNullArgThrowsException() {
		Throwable thrown = catchThrowable(() -> { clientService.getClientOrders(null); });
		assertThat(thrown).hasMessageContaining("Client cannot be null!");
	}
	
	@Test
	public void getClientOrdersWhenClientDoesntExistThrowsException() {
		Throwable thrown = catchThrowable(() -> { clientService.getClientOrders(client); });
		assertThat(thrown).hasMessageContaining("Client does not exist!");
	}
	
	@Test
	public void findByIdReturnsClient() {
		clientStorage.add(client);
		
		Client result = clientService.findById(client.getId());
		
		assertEquals(client, result);
	}
	
	@Test
	public void findByIdWithArgNullThrowsException() {
		Throwable thrown = catchThrowable(() -> { clientService.findById(null); });
		assertThat(thrown).hasMessageContaining("ID cannot be null!");
	}
	
	@Test
	public void clientExistsWithArgNullThrowsException() {
		Throwable thrown = catchThrowable(() -> { clientService.clientExists(null); });
		assertThat(thrown).hasMessageContaining("Client cannot be null!");
	}
	
	@Test
	public void clientExistsWhenClientExistReturnsTrue() {
		clientStorage.add(client);
		
		boolean result = clientService.clientExists(client);
		
		assertTrue(result);
	}
	
	@Test
	public void clientExistsWhenClientDoesntExistReturnsFalse() {
		boolean result = clientService.clientExists(client);
		
		assertFalse(result);
	}
	
	@After
    public void clean() {
	    clientStorage = null;
	    orderStorage = null;
	    orderItemStorage = null;
	    orderService = null;
	    clientService = null;
	    client = null;
    }
}
