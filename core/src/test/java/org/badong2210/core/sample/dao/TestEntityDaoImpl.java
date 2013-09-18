package org.badong2210.core.sample.dao;

import org.badong2210.core.dao.BaseDaoImpl;
import org.badong2210.core.sample.beans.TestEntity;
import org.badong2210.core.dao.BaseDaoImpl;
import org.badong2210.core.sample.beans.TestEntity;
import org.badong2210.core.dao.BaseDaoImpl;

/**
 * Dao for {@link org.badong2210.core.sample.beans.TestEntity} class.
 */
public class TestEntityDaoImpl extends BaseDaoImpl<TestEntity> implements TestEntityDao {
    public TestEntityDaoImpl() {
        super(TestEntity.class);
    }
}
