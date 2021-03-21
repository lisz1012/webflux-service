package com.lisz;

import com.lisz.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PersonDao extends ReactiveMongoRepository<Person, String> {

	Flux<Person> findByNameLike(String name);
}
