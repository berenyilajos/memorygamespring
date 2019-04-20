package hu.fourdsoft.memorygame;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "hu.fourdsoft.memorygame.common.model")
@EnableJpaRepositories(basePackages = "hu.fourdsoft.memorygame.dao")
@ComponentScan(basePackages = "hu.fourdsoft.memorygame")
@Configuration
public class MemorygameConfig {
}
