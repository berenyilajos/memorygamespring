package hu.fourdsoft.memorygame.configuration;

import javax.sql.DataSource;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "hu.fourdsoft.memorygame.dao",
        entityManagerFactoryRef = "memorygameEntityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class MemorygameDataSourceConfiguration {
	
	@Bean
    @Primary
    @ConfigurationProperties("app.datasource.memorygame")
    public DataSourceProperties memorygameDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "memorygameDataSource")
    @Primary
    public DataSource memorygameDataSource() {
        DataSourceProperties properties = memorygameDataSourceProperties();
        PoolingDataSource bitronixDataSourceBean = new PoolingDataSource();
        bitronixDataSourceBean.setMaxPoolSize(5);
        bitronixDataSourceBean.setUniqueName("MemoryGameDS");
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

    @Bean(name = "memorygameEntityManagerFactory")
    @Primary
    @DependsOn({"transactionManager", "memorygameDataSource"})
    public LocalContainerEntityManagerFactoryBean memorygameEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource memorygameDataSource) throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.transaction.factory_class", "jta");
        LocalContainerEntityManagerFactoryBean bean = builder
                .dataSource(memorygameDataSource)
                .packages("hu.fourdsoft.memorygame.common.model")
                .persistenceUnit("memorygame")
                .jta(true)
                .properties(properties)
                .build();
        Properties prop = new Properties();
        prop.put("hibernate.transaction.factory_class", "jta");
        prop.put("hibernate.transaction.jta.platform", "hu.fourdsoft.memorygame.jta.BitronixJtaPlatform");
        bean.setJpaProperties(prop);

        return bean;
    }

}
