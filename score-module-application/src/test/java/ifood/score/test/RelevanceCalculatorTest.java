package ifood.score.test;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import ifood.score.menu.Category;
import ifood.score.order.Item;
import ifood.score.order.Order;
import ifood.score.service.RelevanceCalculatorService;


@RunWith(SpringRunner.class)
public class RelevanceCalculatorTest {
	
	private final static String UUID_TEST = "6208e2fd-45c3-4013-a69a-5f54cb249be0";
	private DecimalFormat decimalFormat;
	private RelevanceCalculatorService service;
	
	@Before
	public void init(){
		 service = new RelevanceCalculatorService();
		 decimalFormat = new DecimalFormat(".######");
	}
	
	@Test
	public void testRelevanceCalc(){		
		Order order = buildOrder();
		assertThat(service).isNotNull();		
		assertThat(decimalFormat.format(service.getCategoryRelevance(order).get(Category.PIZZA).doubleValue())).isEqualTo("58,131836");		
		assertThat(decimalFormat.format(service.getMenuItemRelevance(order).get(UUID.fromString(UUID_TEST)).doubleValue())).isEqualTo("30,512858");			
	}
	

	private Order buildOrder(){		
		Order order = new Order();
		order.setUuid(UUID.randomUUID());
		order.setRestaurantUuid(UUID.randomUUID());
		order.setCustomerUuid(UUID.randomUUID());
		order.setAddressUuid(UUID.randomUUID());
		order.setConfirmedAt(new Date());
		order.setItems(builItems());
				
		return order;
	}
	
	private List<Item> builItems() {
		List<Item> items = new LinkedList<>();		
		
		Item item1 = new Item();
		item1.setMenuUuid(UUID.randomUUID());
		item1.setMenuCategory(Category.PIZZA);
		item1.setMenuUnitPrice(new BigDecimal(26.0));
		item1.setQuantity(1);
		
		Item item2 = new Item();
		item2.setMenuUuid(UUID.fromString(UUID_TEST));
		item2.setMenuCategory(Category.VEGAN);
		item2.setMenuUnitPrice(new BigDecimal(3));
		item2.setQuantity(3);
		
		Item item3 = new Item();
		item3.setMenuUuid(UUID.randomUUID());
		item3.setMenuCategory(Category.PIZZA);
		item3.setMenuUnitPrice(new BigDecimal(23));
		item3.setQuantity(1);
		
		
		items.add(item1);
		items.add(item2);
		items.add(item3);
		return items;
	}	

}
