package com.example.gomesrodris.archburgers.tools.migration;

import com.example.gomesrodris.archburgers.adapters.driven.infra.ConnectionPool;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DatabaseMigration {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMigration.class);

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("Usage: DatabaseMigration <driverClass> <dbUrl> <dbUser> <dbPass>");
            System.exit(1);
        }

        new DatabaseMigration().runMigrations(args[0], args[1], args[2], args[3]);
    }

    public void runMigrations(String driverClass, String dbUrl, String dbUser, String dbPass) throws Exception {
        LOGGER.info("Starting Database migrations");

        try (var connectionPool = new ConnectionPool(driverClass, dbUrl, dbUser, dbPass);
             var connection = connectionPool.getConnection()) {

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase("liquibase/dbchangelog.yml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        }

        LOGGER.info("Database migration complete");
    }
}
