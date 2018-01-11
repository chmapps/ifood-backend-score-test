package ifood.score.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ifood.score.enums.OrderOperation;
import ifood.score.menu.Category;
import ifood.score.order.Order;
import ifood.score.relevance.CategoryRelevance;
import ifood.score.relevance.MenuRelevance;
import ifood.score.relevance.OrderRelevance;
import ifood.score.relevance.OrderRelevanceStatus;
import ifood.score.repository.OrderRelevanceRepository;
import ifood.score.repository.RelevanceScoreRepository;

@Service
public class OrderRelevanceService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderRelevanceService.class);
	
	@Autowired
	private OrderRelevanceRepository orderRelevanceRepository;
	
	@Autowired
	private RelevanceCalculatorService relevanceCalculator;
	
	@Autowired
	private RelevanceScoreRepository relevanceScoreRepository; 
	
	/**
	 *  Update relevance menu item and category.
	 *  
	 * @param lstOrderRelevance
	 */
	public void updateRelevance(List<OrderRelevance> lstOrderRelevance, OrderOperation operation, OrderRelevanceStatus status){
		lstOrderRelevance.forEach(order->{
			//process categories
			updateCategoryRelevance(order.getCategories(),operation);
			
			//process menu items
			updateMenuRelevance(order.getMenu(), operation);
			
			//update order to status			
			orderRelevanceRepository.updateStatus(order, status);
			LOGGER.info(String.format("Updating order status %s to %s" ,order.getUuid(), status));
		});		
	}
	
	private void updateCategoryRelevance(List<CategoryRelevance> lstCategoryRelevance, OrderOperation operation){
		for (CategoryRelevance category: lstCategoryRelevance) {			
			relevanceScoreRepository.updateRelevanceCategoryScore(category.getCategory().toString(), category.getRelevance(),operation);		
			LOGGER.info(String.format("%s Category %s relevance: %.5f ",operation, category.getCategory(), category.getRelevance()));			
		}			
	}
	
	private void updateMenuRelevance(List<MenuRelevance> lstMenuRelevance, OrderOperation operation){
		lstMenuRelevance.forEach(menu ->{			
			relevanceScoreRepository.updateRelevanceMenuScore(menu.getUuid().toString(), menu.getRelevance(),operation);		
			LOGGER.info(String.format("%s Menu %s relevance: %.5f ",operation, menu.getUuid(), menu.getRelevance()));
		});			
	}
	
	
	
	
	/**
	 *  Calculate score for menu item , category, and insert order into repository
	 * @param order
	 */
	public void createOrder(Order order){		
		//retrieve all calculated relevance values for order and save them		
		OrderRelevance orderRelevance = builOrderRelevance(order, 
				builCategoryRelevance(relevanceCalculator.getCategoryRelevance(order)), 
				builMenuRelevance(relevanceCalculator.getMenuItemRelevance(order)));
		orderRelevanceRepository.save(orderRelevance);
	}
	
	/**
	 * Mark order TO_CANCEL, if already processed
	 * otherwise update order to CANCELLED 
	 * 
	 * @param uuidOrder
	 */
	public void cancelOrder(UUID uuidOrder){
		final OrderRelevanceStatus statusToUpdate;
		//update status order to OrdersStatus.CANCELLED
		OrderRelevance orderRelevance = orderRelevanceRepository.getOneByUUID(uuidOrder);
		//mark to decrement process, if already processed 
		if (orderRelevance.getStatus().equals(OrderRelevanceStatus.PROCESSED)){
			statusToUpdate = OrderRelevanceStatus.TO_CANCEL;
		}else{
			statusToUpdate = OrderRelevanceStatus.CANCELLED;
		}
		orderRelevanceRepository.updateStatus(orderRelevance, statusToUpdate);
		LOGGER.info(String.format("Updating order status %s to %s", uuidOrder, statusToUpdate));
	}
	
	
	private OrderRelevance builOrderRelevance(Order order, List<CategoryRelevance> categories, List<MenuRelevance> menu){		
		OrderRelevance orderRelevance = new OrderRelevance();
		orderRelevance.setStatus(OrderRelevanceStatus.CREATED);
		orderRelevance.setCreated(order.getConfirmedAt());
		orderRelevance.setUuid(order.getUuid());
		orderRelevance.setMenu(menu);
		orderRelevance.setCategories(categories);
		return orderRelevance;
	}
	
	private List<CategoryRelevance> builCategoryRelevance(Map<Category, BigDecimal> values){
		List<CategoryRelevance> categories = new ArrayList<>();
		values.entrySet().forEach(entry->{
			CategoryRelevance categoryRelevance = new CategoryRelevance();
			categoryRelevance.setCategory(entry.getKey());
			categoryRelevance.setRelevance(entry.getValue());			
			categories.add(categoryRelevance);			
		});		
		return categories;
	}
	
	private List<MenuRelevance> builMenuRelevance(Map<UUID, BigDecimal> values){
		List<MenuRelevance> menu = new ArrayList<>();
		values.entrySet().forEach(entry->{
			MenuRelevance menuRelevance = new MenuRelevance();
			menuRelevance.setUuid(entry.getKey());
			menuRelevance.setRelevance(entry.getValue());
			menu.add(menuRelevance);			
		});		
		return menu;
	}
	


}
