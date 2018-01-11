package ifood.score.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import ifood.score.enums.FindOperation;
import ifood.score.menu.Category;
import ifood.score.relevance.CategoryRelevance;
import ifood.score.relevance.RelevanceScore;
import ifood.score.repository.RelevanceScoreRepository;

@Service
public class CategoryScoreService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryScoreService.class);
	
	@Autowired
	private RelevanceScoreRepository relevanceScoreRepository;
		
	@Cacheable(sync=true, value="category-score")
	public BigDecimal getScore(String strCategory) {
		try {
			Category.valueOf(strCategory);
			RelevanceScore relevanceScore = relevanceScoreRepository.getCategoryScoreByKey(strCategory);
			return returRelevance(relevanceScore);
		} catch (IllegalArgumentException e) {
			LOGGER.info(String.format("Category not found %s", strCategory));
			return null;
		}			
	}
	
	@Cacheable(sync=true, value="category-parameter-score")
	public List<CategoryRelevance> findCategoryRelevance(Double score, FindOperation findOperation) {
		return relevanceScoreRepository.getCategoryScoreByValue(score, findOperation);	
	}
	
	
	private BigDecimal returRelevance(RelevanceScore relevanceScore) {
		if (relevanceScore==null) {
			return null;
		}
		return relevanceScore.getSumRelevance().divide(new BigDecimal(relevanceScore.getTotalCount()), BigDecimal.ROUND_HALF_EVEN);
	}
	
	
	
}
