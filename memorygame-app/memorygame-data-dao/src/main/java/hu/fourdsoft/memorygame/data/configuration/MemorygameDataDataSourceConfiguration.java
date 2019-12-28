package hu.fourdsoft.memorygame.data.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "hu.fourdsoft.memorygame.data.dao",
        entityManagerFactoryRef = "memorygameDataEntityManagerFactory",
        transactionManagerRef= "memorygameDataTransactionManager"
)
public class MemorygameDataDataSourceConfiguration {
	
	@Bean
    @ConfigurationProperties("app.datasource.memorygame.data")
    public DataSourceProperties memorygameDataDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.memorygame.data.configuration")
    public DataSource memorygameDataDataSource() {
        return memorygameDataDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "memorygameDataEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean memorygameDataEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(memorygameDataDataSource())
                .packages("hu.fourdsoft.memorygame.common.data.model")
                .persistenceUnit("memorygameData")
                .build();
    }

    @Bean
    public PlatformTransactionManager memorygameDataTransactionManager(
            final @Qualifier("memorygameDataEntityManagerFactory") LocalContainerEntityManagerFactoryBean memorygameDataEntityManagerFactory) {
        return new JpaTransactionManager(memorygameDataEntityManagerFactory.getObject());
    }

}
