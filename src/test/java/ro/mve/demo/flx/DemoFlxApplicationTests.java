package ro.mve.demo.flx;

import static java.lang.String.format;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoFlxApplicationTests {

	static Consumer<Object> print = System.out::println;
	static String URI = "http://localhost:%d";

	@LocalServerPort
	private int port;

	@Test
	public void contextLoads() throws IOException {
		System.out.println("*** port:" + port);
		Flux<Integer> k = Flux.just(1, 2, 3, 4, 5);
		k.map(i -> i * 10).subscribe(print);
		k.map(i -> i + 5).subscribe(print);

		Stream<Integer> j = Arrays.asList(1, 2, 3, 4, 5).stream();
		j.map(i -> i * 10).forEach(print);
		// j.map(i -> i + 5).forEach(System.out::println); fails here
		System.in.read();
	}

	@Test
	public void main() throws IOException {
		WebClient client = WebClient.create(format(URI, port));

		

	}

}
