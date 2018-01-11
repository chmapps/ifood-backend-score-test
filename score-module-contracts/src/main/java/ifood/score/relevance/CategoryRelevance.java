package ifood.score.relevance;

import java.io.Serializable;
import java.math.BigDecimal;

import ifood.score.menu.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of={"category"})
public class CategoryRelevance implements Serializable {

	private static final long serialVersionUID = -4734398651257844362L;

	private Category category;	
	private BigDecimal relevance;
		

}
