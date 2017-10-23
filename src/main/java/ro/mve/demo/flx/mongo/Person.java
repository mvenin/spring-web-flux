package ro.mve.demo.flx.mongo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Document
public class Person {

	private @Id String id;
	private final String firstname;
	private final String lastname;
	private final int age;
	

	
}

