package hu.fourdsoft.memorygame.jta;

import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;
import org.junit.Assert;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

public class BitronixJtaPlatform extends AbstractJtaPlatform {

    private static TransactionManager transactionManager;
    private static UserTransaction userTransaction;

    @Override
    protected TransactionManager locateTransactionManager() {
        Assert.assertNotNull("TransactionManager has not been setted", transactionManager);
        return transactionManager;
    }

    @Override
    protected UserTransaction locateUserTransaction() {
        Assert.assertNotNull("UserTransaction has not been setted", userTransaction);
        return userTransaction;
    }

    public static void setBitronixTransactionManager(TransactionManager transactionManager) {
        BitronixJtaPlatform.transactionManager = transactionManager;
    }

    public static void setBitronixUserTransaction(UserTransaction userTransaction) {
        BitronixJtaPlatform.userTransaction = userTransaction;
    }
}
