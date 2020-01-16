package hu.fourdsoft.memorygame;

import hu.fourdsoft.memorygame.configuration.MemorygameConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemorygameApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Class<?>[]{MemorygameApplication.class, MemorygameConfiguration.class}, args);
	}

}
