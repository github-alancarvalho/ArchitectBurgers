package com.example.gomesrodris.archburgers.domain.testUtils;

import com.example.gomesrodris.archburgers.domain.support.TransactionManager;

import java.util.function.Supplier;

public class DummyTransactionManager implements TransactionManager {
    @Override
    public <T> T runInTransaction(Supplier<T> task) throws Exception {
        return task.get();
    }
}
