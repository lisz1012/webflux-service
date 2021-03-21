package com.lisz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
配置类，通过URI找到所对应的Handler
 匹配URI的规则，包括请求方法：PUT/POST
 */
// WebFlux是在Netty之上实现了HTTP协议。网络IO是微服务架构中最消耗时间的部分，异步响应式可以使得线程在等待网络IO的时候干点别的事情，压榨CPU性能和吞吐量
// 虽然不等待了，但也要考虑背压的问题，不能无限制的往其他的微服务发请求，别把后面给压死了
// CTO参与决策，把握大方向，在技术层面能帮公司省钱或者挣钱
@Configuration
public class FluxRouter {

	// 每一个Router对应的是一个Handler，每个Handler代表一个处理器。Router对应的就是Controller类最上面的那个@RestController和@RequestMapping
	// Controller里面每个方法对应的Mapping也是Router做的路由,Router会找Handler这个类或者方法。Handler表示真正的处理器，Controller里的方法
	@Bean
	public RouterFunction<ServerResponse> routerFlux(FluxHandler fluxHandler) {
		return RouterFunctions
				.route(
						RequestPredicates.path("/abc")  //匹配规则
										 .and(RequestPredicates.method(HttpMethod.POST))
										 .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
						request -> ServerResponse.ok().body(BodyInserters.fromValue("haha"))
				)
				.andRoute(RequestPredicates.GET("/hello"), fluxHandler::get)
				.andRoute(RequestPredicates.GET("/hi"), fluxHandler::getJSON)
				.andRoute(RequestPredicates.GET("/param"), fluxHandler::getParam)
				.andRoute(RequestPredicates.GET("/params"), fluxHandler::getParams)
				//去URI里的变量/属性值，匹配的时候格式必须要一致，两个pathVariable都要写，少一个都不行，否则404
				.andRoute(RequestPredicates.GET("/path/{id}/{name}"), fluxHandler::getPath)
				//写了这个Route之后，用_分隔的情况就也能匹配到了
				.andRoute(RequestPredicates.GET("/path/{id}_{name}"), fluxHandler::getPath)
				.andRoute(RequestPredicates.GET("/status"), fluxHandler::getStatus)
				.andRoute(RequestPredicates.GET("/redirect"), fluxHandler::getRedirect)
				.andRoute(RequestPredicates.GET("/flux"), fluxHandler::flux);
	}
}
