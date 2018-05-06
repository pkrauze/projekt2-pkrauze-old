package storages;

import java.util.List;

import models.Client;
import models.Order;

public interface ClientStorage {
	void add(Client client);
	Client findById(long id);
	List<Client> getAllClients();
	void update(Client client);
	void delete(Client client);
	List<Order> getOrders(Client client);
}
