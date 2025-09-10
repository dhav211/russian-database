package com.havlin.daniel.russian;

import com.havlin.daniel.russian.services.dictionary.DefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RussianDatabaseApplication {
	public static void main(String[] args) {
		SpringApplication.run(RussianDatabaseApplication.class, args);
	}

}
