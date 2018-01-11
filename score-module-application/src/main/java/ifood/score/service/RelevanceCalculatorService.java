package ifood.score.service;

import java.math.BigDecimal;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


import ifood.score.menu.Category;
import ifood.score.order.Item;
import ifood.score.order.Order;

@Service
public class RelevanceCalculatorService {
	
	
	public Map<Category, BigDecimal> getCategoryRelevance(Order order){
		
		Map<Category, BigDecimal> categoryRelevance = new HashMap<>();		
		//retrieve unique categories from order
		Set<Category> categories = order.getItems().stream().map(Item::getMenuCategory).distinct().collect(Collectors.toSet());		
		categories.stream().forEach(category ->{			
			//get totals from selected category				
			Integer totalQuantity = order.getItems().stream().filter(entry -> category.equals(entry.getMenuCategory())).mapToInt(x -> x.getQuantity()).sum();
			Double totalPrice = order.getItems().stream().filter(entry -> category.equals(entry.getMenuCategory())).mapToDouble(x -> getTotalItemPrice(x).doubleValue()).sum();
			
			Double iq = Double.valueOf(totalQuantity) / sumTotalOrderItem(order);
			Double ip = totalPrice / sumTotalOrderPrice(order);
			categoryRelevance.put(category, calcRelevance(iq, ip));			
		});
		
		return categoryRelevance;
	}
	
	public Map<UUID, BigDecimal> getMenuItemRelevance(Order order){
		
		Map<UUID, BigDecimal> menuItemRelevance = new HashMap<>();		
		order.getItems().stream().forEach(item ->{
			Double iq = Double.valueOf(item.getQuantity()) / sumTotalOrderItem(order);
			Double ip = getTotalItemPrice(item) / sumTotalOrderPrice(order);	
			menuItemRelevance.put(item.getMenuUuid(), calcRelevance(iq, ip));
		});
		
		return menuItemRelevance;
	}
		
	private BigDecimal calcRelevance(Double iq, Double ip){		
		Double relevance = Math.sqrt(iq*ip*10000.0);		
		return new BigDecimal(relevance);					
	}

	private Double sumTotalOrderPrice(Order order){		
		return order.getItems().stream().mapToDouble(p -> getTotalItemPrice(p).doubleValue()).sum();		
	}
	
	private Integer sumTotalOrderItem(Order order){
		return order.getItems().stream().mapToInt(q -> q.getQuantity()).sum();
	}
	
	private Double getTotalItemPrice(Item item){
		if (item ==null){
			return 0.0;
		}
		return item.getMenuUnitPrice().doubleValue() * item.getQuantity();
		
	}
}
