package ro.mve.demo.flx;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import ro.mve.demo.flx.mongo.Person;
import ro.mve.demo.flx.mongo.ReactivePersonRepository;

/**
 * Simple configuration that registers a {@link LoggingEventListener} to
 * demonstrate mapping behavior when streaming data.
 *
 */
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, 
		EmbeddedMongoAutoConfiguration.class })
//@AutoConfigureAfter(EmbeddedMongoAutoConfiguration.class)
@EnableReactiveMongoRepositories(considerNestedRepositories = true)
@RequiredArgsConstructor
@Configuration
@EnableTransactionManagement
public class DemoFlxApplication extends AbstractReactiveMongoConfiguration implements CommandLineRunner {

	private final Environment environment;
	
	@Autowired ReactivePersonRepository repository;
	@Autowired ReactiveMongoTemplate template;

	@Override
	public void run(String... args) throws Exception {

		template.createCollection(Person.class, new CollectionOptions(10000L, 10000L, true));

		String[] names = { "Eddard", "Catelyn", "Jon", "Rob", "Sansa", "Aria", "Bran", "Rickon" };

		Random ramdom = new Random();
		Flux<Person> starks = Flux.fromStream(Stream.generate(() -> names[ramdom.nextInt(names.length)]).map(Person::new));

		Flux.interval(Duration.ofSeconds(1)) //
				.zipWith(starks) //
				.map(Tuple2::getT2) //
				.flatMap(repository::save) //
				.doOnNext(System.out::println) //
				//.subscribe()
				;

		System.out.println("Winter is Coming!");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoFlxApplication.class, args);
	}

	@Bean
	public LoggingEventListener mongoEventListener() {
		return new LoggingEventListener();
	}

	@Bean
	//@DependsOn("embeddedMongoServer")
	public MongoClient mongoClient() {
		return MongoClients.create(String.format("mongodb://localhost:%d", 27017));
	}

	@Override
	protected String getDatabaseName() {
		return "micro20";
	}
}
