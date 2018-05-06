package mocks;

import java.util.ArrayList;
import java.util.List;

import models.Client;
import models.Order;
import storages.ClientStorage;

public class MockClientStorage implements ClientStorage {
	private List<Client> clients = new ArrayList<Client>();
	
	@Override
	public void add(Client client) {
		clients.add(client);
	}

	@Override
	public Client findById(long id) {
		return clients.stream().filter(c -> c.getId() == id).findAny().orElse(null);
	}

	@Override
	public List<Client> getAllClients() {
		return clients;
	}

	@Override
	public void update(Client client) {
		Client oldClient = findById(client.getId());
		clients.remove(oldClient);
		clients.add(client);
	}

	@Override
	public void delete(Client client) {
		clients.remove(client);
	}

	@Override
	public List<Order> getOrders(Client client) {
		Client foundClient = findById(client.getId());
		if(foundClient != null)
			return foundClient.getOrders();
		return null;
	}
}
