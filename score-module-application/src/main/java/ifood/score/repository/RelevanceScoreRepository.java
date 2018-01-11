package ifood.score.repository;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ifood.score.enums.FindOperation;
import ifood.score.enums.OrderOperation;
import ifood.score.menu.Category;
import ifood.score.relevance.CategoryRelevance;
import ifood.score.relevance.MenuRelevance;
import ifood.score.relevance.RelevanceScore;

@Component
public class RelevanceScoreRepository {
	private Set<RelevanceScore> categoryScore = Collections.synchronizedSet(new HashSet<>());
	private Set<RelevanceScore> menuScore = Collections.synchronizedSet(new HashSet<>());
	
	
	public void updateRelevanceCategoryScore(String key, BigDecimal value, OrderOperation operation) {		
		updateRelevanceScore(key, value, operation, categoryScore);
	}
	public void updateRelevanceMenuScore(String key, BigDecimal value, OrderOperation operation) {		
		updateRelevanceScore(key, value, operation, menuScore);
	}
	
	public void updateRelevanceScore(String key, BigDecimal value, OrderOperation operation,  Set<RelevanceScore> relevanceScore) {
		synchronized (relevanceScore) {
			RelevanceScore score = getScoreByKey(key, relevanceScore);
			final BigDecimal sumRelevance;
			final Integer totalCount;
			
			if (operation.equals(OrderOperation.INCREMENT)) {
				if (score==null) {
					score = new RelevanceScore();
					relevanceScore.add(score);
				}				
				sumRelevance = score.getSumRelevance().add(value);
				totalCount = score.getTotalCount() + 1;
			}else {
				if (score==null) {
					return;
				}
				sumRelevance = score.getSumRelevance().subtract(value);
				totalCount = score.getTotalCount() - 1;				
			}			
			score.setKey(key);
			score.setSumRelevance(sumRelevance);
			score.setTotalCount(totalCount);			
		}		
	}
	
	public RelevanceScore getCategoryScoreByKey(String key){
		return getScoreByKey(key, categoryScore);		
	}
	public RelevanceScore getMenuScoreByKey(String key){
		return getScoreByKey(key, menuScore);		
	}
	
	public RelevanceScore getScoreByKey(String key, Set<RelevanceScore> relevanceScore){
		synchronized (relevanceScore) {
			return relevanceScore.stream().filter(item -> item.getKey().equals(key))
			.findAny()			
			.orElse(null);					
		}
	}
	
	public List<CategoryRelevance> getCategoryScoreByValue(Double score, FindOperation findOperation){
		int limit = 20;
		final Predicate<CategoryRelevance> findPredicate;		
		if (findOperation.equals(FindOperation.ABOVE)) {
			findPredicate = (t -> score < t.getRelevance().doubleValue());
		}else {			
			findPredicate = (t -> score > t.getRelevance().doubleValue());
		}				
		synchronized (categoryScore) {
			return categoryScore.stream()
					.map(item -> {					
						CategoryRelevance categoryRelevance = new CategoryRelevance();
						if (item.getTotalCount().equals(0)) {
							categoryRelevance.setRelevance(BigDecimal.ZERO);
						}else {
							BigDecimal relevance = item.getSumRelevance().divide(new BigDecimal(item.getTotalCount()),BigDecimal.ROUND_HALF_UP);
							categoryRelevance.setRelevance(relevance);	
						}												
						categoryRelevance.setCategory(Category.valueOf(item.getKey()));
						return categoryRelevance;
					}).filter(findPredicate)
					.sorted((o1, o2)->o1.getRelevance().compareTo(o2.getRelevance())).limit(limit).collect(Collectors.toList());
		}
	}
	
	
	
	public List<MenuRelevance> getMenuScoreByValue(Double score, FindOperation findOperation){
		int limit = 20;
		final Predicate<MenuRelevance> findPredicate;		
		if (findOperation.equals(FindOperation.ABOVE)) {
			findPredicate = (t -> score < t.getRelevance().doubleValue());
		}else {			
			findPredicate = (t -> score > t.getRelevance().doubleValue());
		}		
		synchronized (menuScore) {
			return menuScore.stream()
					.map(item -> {					
						MenuRelevance menuRelevance = new MenuRelevance();
						if (item.getTotalCount().equals(0)) {
							menuRelevance.setRelevance(BigDecimal.ZERO);
						}else {
							BigDecimal relevance = item.getSumRelevance().divide(new BigDecimal(item.getTotalCount()),BigDecimal.ROUND_HALF_UP);
							menuRelevance.setRelevance(relevance);	
						}						
						menuRelevance.setUuid(UUID.fromString(item.getKey()));
						return menuRelevance;
					}).filter(findPredicate)
					.sorted((o1, o2)->o1.getRelevance().compareTo(o2.getRelevance())).limit(limit).collect(Collectors.toList());
		}
	}
}
