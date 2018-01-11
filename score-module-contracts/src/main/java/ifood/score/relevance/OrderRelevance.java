package ifood.score.relevance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
public class OrderRelevance implements Serializable {	

	private static final long serialVersionUID = -6369636050537542333L;
	
	private UUID uuid;
	private Date created;
	private OrderRelevanceStatus status;
	private List<CategoryRelevance> categories;
	private List<MenuRelevance> menu;

}
