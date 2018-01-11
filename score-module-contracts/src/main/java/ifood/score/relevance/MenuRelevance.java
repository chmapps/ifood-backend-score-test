package ifood.score.relevance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of={"uuid"})
public class MenuRelevance implements Serializable {	


	private static final long serialVersionUID = -6369636050537542333L;
	
	private UUID uuid;
	private BigDecimal relevance;

}
