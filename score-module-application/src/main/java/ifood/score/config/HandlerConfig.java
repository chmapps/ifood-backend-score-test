package ifood.score.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import ifood.score.handler.CategoryHandler;
import ifood.score.handler.EchoHandler;
import ifood.score.handler.MenuHandler;

@Configuration
public class HandlerConfig {

	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(EchoHandler echoHandler, CategoryHandler categoryHandler, MenuHandler menuHandler) {
		return route(POST("/echo"), echoHandler::echo)
				.andRoute(GET("/api/v1/score/category/find"), categoryHandler::getScoreByParam)
				.andRoute(GET("/api/v1/score/category/{category}"), categoryHandler::getScoreByCategory)
				.andRoute(GET("/api/v1/score/menu/find"), menuHandler::getScoreByParam)
				.andRoute(GET("/api/v1/score/menu/{uuid}"), menuHandler::getScoreByUUID);
				
	}
}
