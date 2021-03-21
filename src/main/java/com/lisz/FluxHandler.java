package com.lisz;

import com.lisz.entity.Person;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

//单例了，线程不安全. 打开这个FluxHandler是不知道要访问的URL是什么的。很简洁但也很尴尬
@Component
public class FluxHandler {

	//简单值返回
	public Mono<ServerResponse> get(ServerRequest request){ //拿不到ServerResponse容器只传过来ServerRequest, 还必须有这个ServerRequest对象, 方法里不用也要有, 还不能有其他的参数
		return ServerResponse
				.ok()
				.contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromValue("Hello!"));
	}

	public Mono<ServerResponse> getJSON(ServerRequest request){ //拿不到ServerResponse容器只传过来ServerRequest, 还必须有这个ServerRequest对象, 方法里不用也要有, 还不能有其他的参数
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(new Person("1", "xiao ming", 18, 100.00)));
	}

	public Mono<ServerResponse> getParam(ServerRequest request){ //拿不到ServerResponse容器只传过来ServerRequest, 还必须有这个ServerRequest对象, 方法里不用也要有, 还不能有其他的参数
		Optional<String> id = request.queryParam("id");
		System.out.println(id.get());
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(new Person(id.get(), "xiao ming", 18, 110.00)));
	}

	public Mono<ServerResponse> getParams(ServerRequest request) {
		MultiValueMap<String, String> map = request.queryParams();
		List<String> list = map.get("id");
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(list));
	}

	public Mono<ServerResponse> getPath(ServerRequest request) {
		String id = request.pathVariable("id");
		String name = request.pathVariable("name");
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(new Person(id, name, 18, 120.00)));
	}

	public Mono<ServerResponse> getStatus(ServerRequest request) {
		// 下面会异步返回666状态码给框架（而不是用户），当前执行本代码的线程释放，如果有新的线程在这里生成了，
		// 则那个新线程执行完了之后会触发一个新的事件，返回到容器里，把返回值带过去
//		return ServerResponse.status(666)
//				.build();

		// 没有返回值的404，状态码跟返回值是两回事儿
		// return ServerResponse.notFound().build();
		// return ServerResponse.status(404).build();

		// 带返回值（出错提示）的404. 还可以写 MediaType.TEXT_HTML
		return ServerResponse.status(404).body(BodyInserters.fromValue("Sorry, page not found"));
	}

	// 跳转URI
	public Mono<ServerResponse> getRedirect(ServerRequest request) {
		URI uri = null;
		try {
			uri = new URI("http://192.168.1.102:8080/redirect");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return ServerResponse.created(uri).build();
	}

	public Mono<ServerResponse> flux(ServerRequest request) {
		return ServerResponse.ok()
				.contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromValue("Hello!"));
	}
}
