package org.badong2210.core.dbmigrator;

import com.carbonfive.db.jdbc.DatabaseType;
import com.carbonfive.db.migration.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.commons.collections.CollectionUtils.find;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

public class DefaultResourceMigrationResolver extends ResourceMigrationResolver implements MigrationResolver {
    private String migrationsLocation;
    private VersionExtractor versionExtractor;

    public void setMigrationsLocation(String migrationsLocation) {
        super.setMigrationsLocation(migrationsLocation);
        this.migrationsLocation = migrationsLocation;
    }

    public void setVersionExtractor(VersionExtractor versionExtractor) {
        super.setVersionExtractor(versionExtractor);
        this.versionExtractor = versionExtractor;
    }

    private MigrationFactory migrationFactory = new MigrationFactory();

    public DefaultResourceMigrationResolver() {
        super();
    }

    public Set<Migration> resolve(DatabaseType dbType) {
        Set<Migration> migrations = new HashSet<Migration>();

        // Find all resources in the migrations location.
        String convertedMigrationsLocation = convertMigrationsLocation(migrationsLocation, dbType);

        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources;
        try {
            resources = new ArrayList<Resource>(Arrays.asList(patternResolver.getResources(convertedMigrationsLocation)));
        } catch (IOException e) {
            throw new MigrationException(e);
        }

        // Remove resources starting with a '.' (e.g. .svn, .cvs, etc)
        CollectionUtils.filter(resources, new Predicate() {
            public boolean evaluate(Object object) {
                try {
                    return (((Resource) object).isReadable() && !((Resource) object).getFilename().startsWith("."));
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Exception while filtering resource.", e);
                    }
                    return false;
                }
            }
        });

        if (resources.isEmpty()) {
            String message = "No migrations were found using resource pattern '" + migrationsLocation + "'.";
            logger.error(message);
            throw new MigrationException(message);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Found " + resources.size() + " resources: " + collectionToCommaDelimitedString(resources));
        }

        // Extract versions and create executable migrations for each resource.
        for (Resource resource : resources) {
            resource = getModifiedResourceForDbType(dbType, resource);
            String version = versionExtractor.extractVersion(resource.getFilename());
            if (find(migrations, new MigrationVersionPredicate(version)) != null) {
                String message = "Non-unique migration version.";
                logger.error(message);
                throw new MigrationException(message);
            }
            migrations.add(migrationFactory.create(version, resource));
        }

        return migrations;
    }

    private Resource getModifiedResourceForDbType(DatabaseType dbType, Resource resource) {
        Resource modifiedResource = resource;
        try {
            Resource modifiedResource2 = resource.createRelative(dbType.name().toLowerCase() + File.separator + resource.getFilename());
            if (modifiedResource2.exists()) {
                modifiedResource = modifiedResource2;
                logger.info("Overriding resource for " + dbType + " original resource: " + resource.getURL().getFile() + " modified resource:" + modifiedResource.getURL().getFile());
            }

        } catch (IOException e) {
            // Do nothing
        }

        return modifiedResource;
    }

    class MigrationVersionPredicate implements Predicate {
        private final String version;

        public MigrationVersionPredicate(String version) {
            this.version = version;
        }

        public boolean evaluate(Object object) {
            return StringUtils.equalsIgnoreCase(((Migration) object).getVersion(), version);
        }
    }

}
