package ifood.score.repository;


import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ifood.score.relevance.OrderRelevance;
import ifood.score.relevance.OrderRelevanceStatus;

@Component
public class OrderRelevanceRepository {
	
	private Set<OrderRelevance> relevances = Collections.synchronizedSet(new HashSet<>());
	
	public void save(OrderRelevance orderRelevance){			
		relevances.add(orderRelevance);
	}
	
	public void updateStatus(OrderRelevance orderRelevance, OrderRelevanceStatus ordersStatus){
		orderRelevance.setStatus(ordersStatus);
	}	
	
	public List<OrderRelevance> getAllByStatus(OrderRelevanceStatus orderStatus){
		synchronized (relevances) {
			return relevances.parallelStream().filter(entry -> orderStatus.equals(entry.getStatus())).collect(Collectors.toList()); 
		}			
	}
	
	public List<OrderRelevance> getAllBeforeCreatedDateAndStatus(Date date, OrderRelevanceStatus orderStatus){
		Predicate<OrderRelevance> statusFilter = (p -> p.getStatus().equals(orderStatus));
		Predicate<OrderRelevance> createdDateFilter = (p -> p.getCreated().before(date));
		synchronized (relevances) {
			return relevances.parallelStream()
					.filter(statusFilter)
					.filter(createdDateFilter)
					.collect(Collectors.toList());		
		}
	}
	
	public OrderRelevance getOneByUUID(UUID uuid){
		Predicate<OrderRelevance> uuidFiler = (p -> p.getUuid().equals(uuid));
		synchronized (relevances) {
			return relevances.parallelStream()
					.filter(uuidFiler)				
					.findAny()			
					.orElse(null);
		}
	}
	
}
