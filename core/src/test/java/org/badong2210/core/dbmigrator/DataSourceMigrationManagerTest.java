package org.badong2210.core.dbmigrator;

import org.badong2210.core.carbonfive.AbstractTransactionalJunit4DataDrivenTest;
import org.badong2210.core.sample.beans.TestEntity;
import org.badong2210.core.sample.dao.TestEntityDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Test for {@link DataSourceMigrationManager} class.
 */
@AbstractTransactionalJunit4DataDrivenTest.DataSet(locations = "dataset/test-entity-set.xml")
@ContextConfiguration(locations = { "classpath:spring-db.xml" })
public class DataSourceMigrationManagerTest extends AbstractTransactionalJunit4DataDrivenTest {

    @Autowired
    private TestEntityDao dao;

    @Test
    public void testMigrate() throws Exception {
        TestEntity entity = dao.get(1L);

        assertNotNull(entity);
        assertEquals(Long.valueOf(1L), entity.getId());
    }
}
