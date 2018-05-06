package services;

import java.util.List;

import models.Client;
import models.Order;
import storages.ClientStorage;

public class ClientServiceImpl implements ClientService {
	private final ClientStorage clientStorage;
	private final OrderService orderService;
	
	public ClientServiceImpl(ClientStorage clientStorage, OrderService orderService) {
		this.clientStorage = clientStorage;
		this.orderService = orderService;
	}

	public static ClientServiceImpl createWith(final ClientStorage clientStorage, final OrderService orderService) {
		return new ClientServiceImpl(clientStorage, orderService);
	}
	
	@Override
	public void add(Client client) {
		if(client == null)
			throw new IllegalArgumentException("Client cannot be null!");
		if(clientExists(client))
			throw new IllegalArgumentException("Client exists!");
		
		clientStorage.add(client);
	}

	@Override
	public void update(Client client) {
		if(client == null)
			throw new IllegalArgumentException("Client cannot be null!");
		if(!clientExists(client))
			throw new IllegalArgumentException("Client does not exist!");
		
		clientStorage.update(client);
	}

	@Override
	public void delete(Client client) {
		if(client == null)
			throw new IllegalArgumentException("Client cannot be null!");
		if(!clientExists(client))
			throw new IllegalArgumentException("Client does not exist!");
		
		for(Order order : client.getOrders()) {
			orderService.delete(order);
		}
		clientStorage.delete(client);
	}

	@Override
	public List<Client> getAllClients() {
		return clientStorage.getAllClients();
	}

	@Override
	public List<Order> getClientOrders(Client client) {
		if(client == null)
			throw new IllegalArgumentException("Client cannot be null!");
		if(!clientExists(client))
			throw new IllegalArgumentException("Client does not exist!");
		
		return clientStorage.getOrders(client);
	}

	@Override
	public Client findById(Long id) {
		if(id == null)
			throw new IllegalArgumentException("ID cannot be null!");
		
		return clientStorage.findById(id);
	}

	@Override
	public boolean clientExists(Client client) {
		if(client == null)
			throw new IllegalArgumentException("Client cannot be null!");
		
		if(findById(client.getId()) != null)
			return true;
		return false;
	}
}
