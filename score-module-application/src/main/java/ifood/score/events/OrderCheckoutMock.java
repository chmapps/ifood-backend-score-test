package ifood.score.events;

import static ifood.score.mock.generator.RandomishPicker._int;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ifood.score.mock.generator.order.OrderPicker;
import ifood.score.order.Order;

@Service
public class OrderCheckoutMock {

	@Autowired
	JmsTemplate jmsTemplate;
	
	ConcurrentLinkedQueue<UUID> cancellantionQueue = new ConcurrentLinkedQueue<>();
	
	private static OrderPicker picker = new OrderPicker();
	
	@Scheduled(fixedRate=1*1000)
	public void checkoutFakeOrder(){
		IntStream.rangeClosed(1, _int(2, 12)).forEach(t -> {
			Order order = picker.pick();
			if(_int(0, 20) % 20 == 0){
				cancellantionQueue.add(order.getUuid());
			}
			jmsTemplate.convertAndSend("checkout-order", order);
		});
	}
	
	@Scheduled(fixedRate=10*1000)
	public void cancelFakeOrder(){
		IntStream.range(1, _int(2, cancellantionQueue.size() > 2 ? cancellantionQueue.size() : 2)).forEach(t ->{
			UUID orderUuid = cancellantionQueue.poll();
			if(orderUuid != null){
				jmsTemplate.convertAndSend("cancel-order", orderUuid);
			}
		});
	}
	
}
