package ifood.score.events;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ifood.score.enums.OrderOperation;
import ifood.score.relevance.OrderRelevance;
import ifood.score.relevance.OrderRelevanceStatus;
import ifood.score.repository.OrderRelevanceRepository;
import ifood.score.service.OrderRelevanceService;

@Service
public class OrderRelevanceProcessorEvent {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderRelevanceProcessorEvent.class);
	
	private static final Integer MONTHS_TO_EXPIRES=-1;
	
	@Autowired
	private OrderRelevanceRepository orderRelevanceRepository;
	
	@Autowired
	private OrderRelevanceService orderRelevanceService;
			
	@Scheduled(fixedRate=2*1000)
	public void processCreated(){		
		//retrieve new orders to process
		List<OrderRelevance> lstOrderRelevanceCreated = orderRelevanceRepository.getAllByStatus(OrderRelevanceStatus.CREATED);
		LOGGER.info(String.format("Process relevance for %d created orders",lstOrderRelevanceCreated.size()));		
		//processing new values
		orderRelevanceService.updateRelevance(lstOrderRelevanceCreated, OrderOperation.INCREMENT, OrderRelevanceStatus.PROCESSED);
		
	}
	

	@Scheduled(fixedRate=10*1000)
	public void processCancelled(){
		//retrieve cancelled orders to process
		List<OrderRelevance> lstOrderRelevanceCancelled = orderRelevanceRepository.getAllByStatus(OrderRelevanceStatus.TO_CANCEL);
		LOGGER.info(String.format("Process relevance for %d cancelled orders",lstOrderRelevanceCancelled.size()));
		//processing old values
		orderRelevanceService.updateRelevance(lstOrderRelevanceCancelled, OrderOperation.DECREMENT, OrderRelevanceStatus.CANCELLED);
		
	}
	
	@Scheduled(fixedRate=10*1000)
	public void processExpired(){
		//retrieve expired orders to process
		List<OrderRelevance> lstOrderRelevanceExpired = orderRelevanceRepository.getAllBeforeCreatedDateAndStatus(getExpirationDate(new Date()), OrderRelevanceStatus.PROCESSED);
		LOGGER.info(String.format("Process relevance for %d expired orders",lstOrderRelevanceExpired.size()));
		//processing old values		
		orderRelevanceService.updateRelevance(lstOrderRelevanceExpired, OrderOperation.DECREMENT, OrderRelevanceStatus.EXPIRED);
	}

					
	private Date getExpirationDate(Date confirmedDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(confirmedDate);
		calendar.add(Calendar.MONTH, MONTHS_TO_EXPIRES); 
		return calendar.getTime();
	}
	
}
