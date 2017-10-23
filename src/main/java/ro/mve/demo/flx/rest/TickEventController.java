package ro.mve.demo.flx.rest;

import java.time.Duration;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class TickEventController {
	
	@GetMapping("/tick/tail")
	Flux<String> tick() {
		  return Flux.interval(Duration.ofSeconds(1)).map(x -> new Date() + " : -) "  )
				  .take(10)
				  .log()
				  ;
	}

}


