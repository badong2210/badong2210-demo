package org.badong2210.core.dbmigrator;

import com.carbonfive.db.migration.Migration;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.SortedSet;

/**
 * Migration script creator.
 */
public class MigrationScriptBuilder {

    private String domain;

    private SortedSet<Migration> pending;

    public MigrationScriptBuilder setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public MigrationScriptBuilder setMigrations(SortedSet<Migration> pending) {
        this.pending = pending;
        return this;
    }

    public String build() {
        StringBuilder migrationContent = new StringBuilder(1024);
        String NL = System.getProperty("line.separator");

        for (Migration migration : pending) {
            migrationContent.append("/* Migration Script: ").append(migration.getFilename()).append(" */");
            migrationContent.append(NL);
            migrationContent.append(getFileContents(migration.getFilename()));
            migrationContent.append(NL);
            migrationContent.append(generateInsertSchemaVersionScript(migration.getVersion()));
            migrationContent.append(NL);
        }

        BufferedWriter f = null;
        File temp;
        String path = null;
        try {
            // Create temp file.
            temp = File.createTempFile(String.format("migration_script_%s_", domain), ".sql");

            // Write to temp file
            f = new BufferedWriter(new FileWriter(temp));
            f.write(migrationContent.toString());
            path = temp.getCanonicalPath();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create a temporary file to write the contents of the pending migrations", e);
        } finally {
            IOUtils.closeQuietly(f);
        }

        return path;
    }

    private String generateInsertSchemaVersionScript(String version) {
        return "insert into " + this.domain + "_" + DataSourceMigrationManager.SCHEMA_VERSION_TABLE + "(version,applied_on,duration) values (" + "'" + version + "'" + ", NOW() , 0);";
    }

    private String getFileContents(String filename) {
        Resource migrationScript = new ClassPathResource(DataSourceMigrationManager.RESOURCE_PATH_DB_MIGRATIONS + this.domain + "/" + filename);

        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;

        try {
            scanner = new Scanner(migrationScript.getInputStream());
            while (scanner.hasNextLine()){
                text.append(scanner.nextLine()).append(NL);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read InputStream of file from a classpath resource. Filename:" + filename, e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return text.toString();
    }
}
