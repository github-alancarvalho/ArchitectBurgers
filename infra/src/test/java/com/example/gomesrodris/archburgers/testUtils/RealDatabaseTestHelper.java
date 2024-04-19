package com.example.gomesrodris.archburgers.testUtils;

import com.example.gomesrodris.archburgers.adapters.driven.infra.ConnectionPool;
import com.example.gomesrodris.archburgers.tools.migration.DatabaseMigration;
import org.testcontainers.containers.PostgreSQLContainer;

public class RealDatabaseTestHelper {
    private PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:12-alpine"
    );

    public void beforeAll() throws Exception {
        postgres.start();

        new DatabaseMigration(postgres.getDriverClassName(),
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()).runMigrations();
    }

    public void afterAll() {
        postgres.stop();
    }

    public ConnectionPool getConnectionPool() {
        return new ConnectionPool(postgres.getDriverClassName(),
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }
}
