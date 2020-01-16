package hu.fourdsoft.memorygame.configuration;

import com.atomikos.icatch.config.UserTransactionService;
import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.J2eeUserTransaction;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import hu.fourdsoft.memorygame.jta.AtomikosJtaPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.Properties;

@ComponentScan(basePackages = "hu.fourdsoft.memorygame")
@Configuration
public class MemorygameConfiguration {

    @Primary
    @Bean(initMethod = "init", destroyMethod = "shutdownForce")
    public UserTransactionService userTransactionService() {
        Properties properties = new Properties();
        properties.put("hibernate.transaction.factory_class", "jta");
        properties.put("hibernate.transaction.jta.platform", "hu.fourdsoft.memorygame.jta.AtomikosJtaPlatform");
        properties.put("com.atomikos.icatch.service", "com.atomikos.icatch.jta.JtaTransactionServicePlugin");
        return new UserTransactionServiceImp(properties);
    }

    @Primary
    @Bean
    @DependsOn("userTransactionService")
    public UserTransaction userTransaction() throws Throwable {
        J2eeUserTransaction userTransaction = new J2eeUserTransaction();
//        UserTransaction userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(1000);
        return userTransaction;
    }

    @Primary
    @Bean(initMethod = "init", destroyMethod = "close")
    @DependsOn("userTransactionService")
    public TransactionManager atomikosUserTransactionManager() throws Throwable {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setStartupTransactionService(false);
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Primary
    @Bean(name = "atomikosJtaPlatform")
    @DependsOn("userTransactionService")
    public AtomikosJtaPlatform atomikosJtaPlatform() throws Throwable {
        AtomikosJtaPlatform atomikosJtaPlatform = new AtomikosJtaPlatform();
        atomikosJtaPlatform.setAtomikusTransactionManager(atomikosUserTransactionManager());
        atomikosJtaPlatform.setAtomikusUserTransaction(userTransaction());

        return atomikosJtaPlatform;
    }

    @Primary
    @Bean(name = "transactionManager")
    @DependsOn("userTransactionService")
    public PlatformTransactionManager transactionManager() throws Throwable {
        AtomikosJtaPlatform atomikosJtaPlatform = atomikosJtaPlatform();
        JtaTransactionManager transactionManager =
                new JtaTransactionManager(atomikosJtaPlatform.getAtomikusUserTransaction(), atomikosJtaPlatform.getAtomikusTransactionManager());
        transactionManager.setAllowCustomIsolationLevels(true);

        return transactionManager;
    }


}
