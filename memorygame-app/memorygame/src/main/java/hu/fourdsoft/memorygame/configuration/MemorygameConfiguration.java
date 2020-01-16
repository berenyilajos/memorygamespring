package hu.fourdsoft.memorygame.configuration;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import hu.fourdsoft.memorygame.jta.BitronixJtaPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;

@ComponentScan(basePackages = "hu.fourdsoft.memorygame")
@Configuration
public class MemorygameConfiguration {

    @Bean(name = "bitronixTransactionManager")
    @DependsOn
    public BitronixTransactionManager bitronixTransactionManager() throws Throwable {
        BitronixTransactionManager bitronixTransactionManager = TransactionManagerServices.getTransactionManager();
        bitronixTransactionManager.setTransactionTimeout(10000);
        BitronixJtaPlatform.setBitronixUserTransaction(bitronixTransactionManager);
        BitronixJtaPlatform.setBitronixTransactionManager(bitronixTransactionManager);
        return bitronixTransactionManager;
    }

    @Bean(name = "transactionManager")
    @DependsOn({"bitronixTransactionManager"})
    public PlatformTransactionManager transactionManager(TransactionManager bitronixTransactionManager) throws Throwable {
        return new JtaTransactionManager(bitronixTransactionManager);
    }

}
