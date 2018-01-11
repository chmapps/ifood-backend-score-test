package ifood.score.handler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ifood.score.enums.FindOperation;
import ifood.score.relevance.MenuRelevance;
import ifood.score.service.MenuItemScoreService;


@Component
public class MenuHandler extends HandlerApp  {	
	
	@Autowired
	MenuItemScoreService menuItemScoreService;
	
	public Mono<ServerResponse> getScoreByUUID(ServerRequest request) {
		String uuid = request.pathVariable("uuid");
		BigDecimal result = menuItemScoreService.getScore(uuid);
		if (result!=null){
			return ServerResponse.ok().body(Mono.just(result), BigDecimal.class);			
		}		
		return ServerResponse.notFound().build();
	}

	
	public Mono<ServerResponse> getScoreByParam(ServerRequest request) {
		
		Double score = extractDoubleParam(request, "score");
		if (score == null) {
			return ServerResponse.badRequest()
				.body(Mono.just("Missing or invalid value for [score] parameter"), String.class);
		}		
		Optional<String> typeParam = request.queryParam("type");
		if (!typeParam.isPresent()) {
			return ServerResponse.badRequest()
				.body(Mono.just("Missing parameter [type]. Permited values 'above/below'"), String.class);
		}
		FindOperation findOperation = FindOperation.getEnum(typeParam.get());
		if (findOperation ==null) {
			return ServerResponse.badRequest()
				.body(Mono.just("Invalid parameter [type]. Permited values 'above/below'"), String.class);
		}
		
		List<MenuRelevance> lstRelevance = menuItemScoreService.findMenuRelevance(score, findOperation);
		if (lstRelevance!=null && !lstRelevance.isEmpty()){
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Flux.fromIterable(lstRelevance), MenuRelevance.class);
		}		
		return ServerResponse.notFound().build();
	}
	
}
