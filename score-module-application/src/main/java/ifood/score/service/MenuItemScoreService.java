package ifood.score.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import ifood.score.enums.FindOperation;

import ifood.score.relevance.MenuRelevance;
import ifood.score.relevance.RelevanceScore;

import ifood.score.repository.RelevanceScoreRepository;

@Service
public class MenuItemScoreService {
	
		
	@Autowired
	RelevanceScoreRepository relevanceScoreRepository;
		
	@Cacheable(sync=true, value="menu-score")
	public BigDecimal getScore(String strUuid) {		
		RelevanceScore relevanceScore = relevanceScoreRepository.getMenuScoreByKey(strUuid);
		return returRelevance(relevanceScore);									
	}
	

	@Cacheable(sync=true, value="menu-parameter-score")
	public List<MenuRelevance> findMenuRelevance(Double score, FindOperation findOperation) {
		return relevanceScoreRepository.getMenuScoreByValue(score, findOperation);	
	}
	
	
	private BigDecimal returRelevance(RelevanceScore relevanceScore) {
		if (relevanceScore==null) {
			return null;
		}
		return relevanceScore.getSumRelevance().divide(new BigDecimal(relevanceScore.getTotalCount()), BigDecimal.ROUND_HALF_EVEN);
	}
	
}
