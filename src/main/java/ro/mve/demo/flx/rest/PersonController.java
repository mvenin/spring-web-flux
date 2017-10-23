package ro.mve.demo.flx.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import ro.mve.demo.flx.mongo.Person;
import ro.mve.demo.flx.mongo.ReactivePersonRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {

	final ReactivePersonRepository mongo;

	@GetMapping("/add")
	Flux<Person> mongoAdd() {
		return Flux.defer(()-> Flux.from(mongo.save(new Person("Marian", "Venin", 40))) )
				.subscribeOn(Schedulers.elastic());
	}

	@GetMapping(value = "/all")  
	Flux<Person> allPersons() {
		return mongoAdd().mergeWith(mongo.findAll());
	}
	
	@GetMapping(value = "/tail", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  
	Flux<Person> streamPersons() {
		return mongoAdd().mergeWith(mongo.findWithTailableCursorBy().share());
	}

}
