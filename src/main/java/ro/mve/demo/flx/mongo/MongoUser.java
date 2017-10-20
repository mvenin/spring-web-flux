package ro.mve.demo.flx.mongo;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Document
public class MongoUser {

	private String id = UUID.randomUUID().toString();
	private String name;
	private String password;
}