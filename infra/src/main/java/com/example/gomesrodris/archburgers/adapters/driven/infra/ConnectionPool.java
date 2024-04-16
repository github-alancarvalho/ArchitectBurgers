package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ConnectionPool implements AutoCloseable {
    private final ComboPooledDataSource cpds;

    public ConnectionPool(String driverClass, String dbUrl, String dbUser, String dbPass) {
        cpds = buildDataSource(driverClass, dbUrl, dbUser, dbPass);
    }

    @Autowired
    public ConnectionPool(Environment environment) {
        String driverClassEnv = environment.getProperty("archburgers.datasource.driverClass");
        String dbUrlEnv = environment.getProperty("archburgers.datasource.dbUrl");
        String dbUserEnv = environment.getProperty("archburgers.datasource.dbUser");
        String dbPassEnv = environment.getProperty("archburgers.datasource.dbPass");

        if (driverClassEnv == null) {
            throw new IllegalStateException("driverClass env is missing");
        }
        if (dbUrlEnv == null) {
            throw new IllegalStateException("dbUrl env is missing");
        }
        if (dbUserEnv == null) {
            throw new IllegalStateException("dbUser env is missing");
        }
        if (dbPassEnv == null) {
            throw new IllegalStateException("dbPass env is missing");
        }

        cpds = buildDataSource(driverClassEnv, dbUrlEnv, dbUserEnv, dbPassEnv);
    }

    public Connection getConnection() {
        try {
            return cpds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Could not get DB connection: " + e.getMessage(), e);
        }
    }

    private ComboPooledDataSource buildDataSource(String driverClass, String dbUrl, String dbUser, String dbPass) {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(driverClass);
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        cpds.setJdbcUrl(dbUrl);
        cpds.setUser(dbUser);
        cpds.setPassword(dbPass);

        cpds.setMinPoolSize(1);
        cpds.setMaxPoolSize(10);

        return cpds;
    }

    @Override
    public void close() {
        cpds.close();
    }
}
