package hu.fourdsoft.memorygame.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "hu.fourdsoft.memorygame.dao",
        entityManagerFactoryRef = "memorygameEntityManagerFactory",
        transactionManagerRef= "memorygameTransactionManager"
)
public class MemorygameDataSourceConfiguration {
	
	@Bean
    @Primary
    @ConfigurationProperties("app.datasource.memorygame")
    public DataSourceProperties memorygameDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.memorygame.configuration")
    public DataSource memorygameDataSource() {
        return memorygameDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "memorygameEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean memorygameEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(memorygameDataSource())
                .packages("hu.fourdsoft.memorygame.common.model")
                .persistenceUnit("memorygame")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager memorygameTransactionManager(
            final @Qualifier("memorygameEntityManagerFactory") LocalContainerEntityManagerFactoryBean memorygameEntityManagerFactory) {
        return new JpaTransactionManager(memorygameEntityManagerFactory.getObject());
    }

}
