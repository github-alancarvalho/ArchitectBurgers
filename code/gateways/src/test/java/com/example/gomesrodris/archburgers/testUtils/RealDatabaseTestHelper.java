package com.example.gomesrodris.archburgers.testUtils;

import com.example.gomesrodris.archburgers.adapters.database.DatabaseConnection;
import com.example.gomesrodris.archburgers.tools.migration.DatabaseMigration;
import org.jetbrains.annotations.VisibleForTesting;
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

    public DatabaseConnection getConnectionPool() {
        return new DatabaseConnection(postgres.getDriverClassName(),
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }

    @VisibleForTesting
    public String getJdbcUrl() {
        return postgres.getJdbcUrl();
    }

    @VisibleForTesting
    public String getJdbcUsername() {
        return postgres.getUsername();
    }

    @VisibleForTesting
    public String getJdbcPassword() {
        return postgres.getPassword();
    }
}
