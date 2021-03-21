package com.lisz;

import com.lisz.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {
	@Autowired
	private PersonDao personDao;

//	@GetMapping("/person/{id}")
//	public Mono<Person> getPerson(@PathVariable String id){
//		System.out.println("id: " + id);
//		return personDao.findById(id).log();
//	}

	// Map形式的传参
//	@GetMapping("/person/{id}")
//	public Mono<Person> getPerson(@PathVariable String id){
//		System.out.println("id: " + id);
//		Map<String, Object> map = new HashMap<>();
//		map.put("id", id);
//		WebClient webClient = WebClient.create("http://192.168.1.102:8080");
//		webClient.get().uri("/person", map).retrieve();
//		return personDao.findById(id).log();
//	}

	// 类似于@PathVariable 或者 @RequestParam 的传参方式
	@GetMapping("/person/{id}")
	public Mono<Person> getPerson(@PathVariable String id){
		System.out.println("id: " + id);
		WebClient webClient = WebClient.create("http://192.168.1.102:8080"); // 可以先写base URI，对于下面发很多不同API请求的时候
		return webClient.get().uri("/person/{id}", id).retrieve().bodyToMono(Person.class).log();
	}

	@GetMapping("/person")
	public Mono<Person> getPerson2(@RequestParam String id){
		System.out.println("id: " + id);
		WebClient webClient = WebClient.create("http://192.168.1.102:8080"); // 可以先写base URI，对于下面发很多不同API请求的时候
		return webClient.get().uri("/person?id={id}", id).retrieve().bodyToMono(Person.class).log();
	}

	@PostMapping("/save")
	public Mono<Person> save(@RequestBody Person person) {
		System.out.println("saving... " + person);
		WebClient webClient = WebClient.create("http://192.168.1.102:8080");
		return webClient.post()
				.uri("/save")
				.body(BodyInserters.fromValue(person))
				.retrieve()
					.onStatus(HttpStatus::is5xxServerError, resp -> {
						return Mono.error(new RuntimeException("Internal error in creation"));
					})
				.bodyToMono(Person.class).log();
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable String id) {
		System.out.println("Deleting..." + id);
		personDao.deleteById(id);
	}

//	@GetMapping("/findAll")
//	public Flux<Person> findAll(){
//		return personDao.findAll().log();
//	}

	@GetMapping("/findAll")
	public Flux<Person> webClient(){
		WebClient webClient = WebClient.create();
		Flux<Person> personFlux = webClient.get().uri("http://192.168.1.102:8080/findAll").retrieve().bodyToFlux(Person.class);
		return personFlux.log();
	}

	// 响应式的，分页就不好使了
//	@GetMapping("/findAllPaged")
//	public Page<Person> findAllPaged(@RequestParam int pageNum, @RequestParam int pageSize){
//		PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
//		return personDao.findAll(pageRequest);
//	}



	// 模糊查询
	@GetMapping("/findLike/{name}")
	public Flux<Person> findLike(@PathVariable String name){
		WebClient webClient = WebClient.create("http://192.168.1.102:8080");
		return webClient.get().uri("/findLike/{name}", name).retrieve().bodyToFlux(Person.class);
	}
}
