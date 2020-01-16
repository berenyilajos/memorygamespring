package hu.fourdsoft.memorygame.data.configuration;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "hu.fourdsoft.memorygame.data.dao",
        entityManagerFactoryRef = "memorygameDataEntityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class MemorygameDataDataSourceConfiguration {
	
	@Bean
    @ConfigurationProperties("app.datasource.memorygame.data")
    public DataSourceProperties memorygameDataDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "memorygameDataDataSource")
    public DataSource memorygameDataDataSource() {
        DataSourceProperties properties = memorygameDataDataSourceProperties();
        PoolingDataSource bitronixDataSourceBean = new PoolingDataSource();
        bitronixDataSourceBean.setMaxPoolSize(5);
        bitronixDataSourceBean.setUniqueName("MemoryGameDataDS");
        bitronixDataSourceBean.setClassName("oracle.jdbc.xa.client.OracleXADataSource");
        Properties xaProperties = new Properties();
        xaProperties.put("URL", properties.getUrl());
        xaProperties.put("user", properties.getUsername());
        xaProperties.put("password", properties.getPassword());
        bitronixDataSourceBean.setDriverProperties(xaProperties);
        bitronixDataSourceBean.setAllowLocalTransactions(true);
        bitronixDataSourceBean.setIgnoreRecoveryFailures(true);
        return bitronixDataSourceBean;
    }

    @Bean(name = "memorygameDataEntityManagerFactory")
    @DependsOn({"transactionManager", "memorygameDataDataSource"})
    public LocalContainerEntityManagerFactoryBean memorygameDataEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("memorygameDataDataSource") DataSource memorygameDataDataSource) throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.transaction.factory_class", "jta");
        properties.put("hibernate.transaction.jta.platform", "hu.fourdsoft.memorygame.jta.BitronixJtaPlatform");
        return builder
                .dataSource(memorygameDataDataSource)
                .packages("hu.fourdsoft.memorygame.common.data.model")
                .persistenceUnit("memorygameData")
                .jta(true)
                .properties(properties)
                .build();
    }

}
