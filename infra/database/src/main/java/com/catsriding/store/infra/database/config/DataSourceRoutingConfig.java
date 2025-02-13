package com.catsriding.store.infra.database.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DataSourceRoutingConfig extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) return "writer";

        return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                ? "reader"
                : "writer";
    }
}
