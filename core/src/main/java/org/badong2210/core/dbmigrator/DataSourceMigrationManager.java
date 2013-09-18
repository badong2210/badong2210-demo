package org.badong2210.core.dbmigrator;

import com.carbonfive.db.migration.Migration;
import com.carbonfive.db.migration.ResourceMigrationResolver;
import com.carbonfive.db.migration.SimpleVersionStrategy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import javax.sql.DataSource;
import java.util.SortedSet;

/**
 * Base implementation for a data source migration manager.
 *
 */
public class DataSourceMigrationManager {
    private static final Logger LOGGER = Logger.getLogger(DataSourceMigrationManager.class);

    public static final String RESOURCE_PATH_DB_MIGRATIONS = "/db/migrations/";

    public static final String SCHEMA_VERSION_TABLE = "schema_version";

    protected SimpleVersionStrategy versionStrategy;

    protected ResourceMigrationResolver migrationResolver;

    protected DataSource dataSource;

    protected String domain;

    protected boolean forceMigrate;

    @Required
    public void setForceMigrate(boolean forceMigrate) {
        this.forceMigrate = forceMigrate;
    }

    @Required
    public void setVersionStrategy(SimpleVersionStrategy versionStrategy) {
        this.versionStrategy = versionStrategy;
    }

    @Required
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Required
    public void setDomain(String domain) {
        this.domain = domain;
        versionStrategy.setVersionTable(domain + "_" + SCHEMA_VERSION_TABLE);
        migrationResolver.setMigrationsLocation("classpath:" + RESOURCE_PATH_DB_MIGRATIONS + domain);
    }

    @Required
    public void setMigrationResolver(ResourceMigrationResolver migrationResolver) {
        this.migrationResolver = migrationResolver;
    }

    /**
     * Start with migration.
     */
    public void migrate() {
        migrateForDataSource(dataSource, false);
    }

    protected void migrateForDataSource(DataSource dataSource, boolean force) {
        com.carbonfive.db.migration.DataSourceMigrationManager manager = new com.carbonfive.db.migration.DataSourceMigrationManager(dataSource);
        manager.setMigrationResolver(migrationResolver);
        manager.setVersionStrategy(versionStrategy);

        if (forceMigrate) {
            LOGGER.info("forceMigrate() returned True. About to run carbon5 migrations for domain: " + this.domain);
            manager.migrate();
        } else if(force) {
            LOGGER.info("forced migration. About to run carbon5 migrations for domain: " + this.domain);
            manager.migrate();
        } else {
            LOGGER.info("mayAutoMigrate() returned False. Not running migrations for domain: " + this.domain);
            SortedSet<Migration> pending = manager.pendingMigrations();

            if(pending.size() != 0) {
                String fileName = new MigrationScriptBuilder()
                        .setDomain(domain)
                        .setMigrations(pending)
                        .build();

                showingPendingMigrations(pending, fileName);

                throw new IllegalStateException("Database schema is not up-to-date for domain: " + this.domain);
            }
        }
    }

    /*
    * Method that shows the files that need to be executed to bring the schema up-to-date
    */
    protected void showingPendingMigrations(SortedSet<Migration> pending, String fileName) {
        System.err.println("------------------------------------------------------------------------------------------------------");
        System.err.println("                                                                                                      ");
        System.err.println("The following migrations have to be run in " + domain + " domain to bring database schema in sync with the deployed code");
        System.err.println("                                                                                                      ");
        for (Migration migration : pending) {
            System.err.println(migration.getFilename());
        }
        System.err.println("                                                                                                      ");
        System.err.println("The contents of the above migration script(s) is now in " + fileName + " . " );
        System.err.println("Please run it to get the database back in sync" );
        System.err.println("------------------------------------------------------------------------------------------------------");
        System.err.flush();
    }
}
