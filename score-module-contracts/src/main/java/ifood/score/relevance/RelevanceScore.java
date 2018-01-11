package ifood.score.relevance;

import java.io.Serializable;
import java.math.BigDecimal;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of={"key"})
public class RelevanceScore implements Serializable {

	private static final long serialVersionUID = 3474598205565867006L;
	
	private String key;
	private BigDecimal sumRelevance;
	private Integer totalCount;		

	public RelevanceScore() {
		this.totalCount=0;
		this.sumRelevance = BigDecimal.ZERO;
	}
}
