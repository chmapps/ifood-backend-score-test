package ifood.score.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.web.reactive.function.server.ServerRequest;


public class HandlerApp {
	/**
	 * convert and return a positive double value with 2 decimals
	 * @param request
	 * @param name
	 * @return converted value, null if is not a valid double String representation
	 */
	public Double extractDoubleParam(ServerRequest request, String name){
		try {			
			Optional<String> param = request.queryParam(name);
			BigDecimal result =new BigDecimal(Double.parseDouble(param.get()));
			if (result.doubleValue()<0){
				return null;
			}
			result.setScale(2, RoundingMode.HALF_UP);
			return result.doubleValue(); 
		} catch (IllegalArgumentException | NoSuchElementException e) {
			return null;
		} 		
	}	
	 	
}
