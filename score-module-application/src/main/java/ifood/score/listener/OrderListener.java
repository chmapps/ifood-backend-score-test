package ifood.score.listener;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import ifood.score.order.Order;
import ifood.score.service.OrderRelevanceService;

@Component
public class OrderListener {
	
	@Autowired
	private OrderRelevanceService service;
	
	@JmsListener(destination = "checkout-order")
	public void receiveOrderMessage(List<Order> orders) {		
		orders.forEach(order ->{
			service.createOrder(order);	
		});			
	}	
	
	@JmsListener(destination = "cancel-order")
	public void receiveCancelledOrderMessage(List<UUID> uuidOrders) {		
		uuidOrders.forEach(order ->{
			service.cancelOrder(order);	
		});			
	}	
	
}
