package org.badong2210.core.dao;

import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseDaoImpl<T extends Serializable> implements BaseDao<T> {

    private static final int MAX_IN_PARAMETER_SIZE = 500;

    private HibernateTemplate hibernateTemplate;
    private JdbcTemplate jdbcTemplate;
    private Class<T> persistenceClass;

    public BaseDaoImpl(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void delete(T entity) {
        getHibernateTemplate().delete(entity);
    }

    public void forceVersionUpdate(T entity) {
        getHibernateTemplate().lock(entity, LockMode.FORCE);
    }

    public void evict(T entity) {
        getHibernateTemplate().evict(entity);
    }

    @SuppressWarnings("unchecked")
    public T get(Serializable key) {
        return (T) getHibernateTemplate().get(persistenceClass, key);
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return getHibernateTemplate().loadAll(persistenceClass);
    }

    public void save(T entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    public void saveAndFlush(T entity) {
        getHibernateTemplate().saveOrUpdate(entity);
        getHibernateTemplate().flush();
    }

    public void flush() {
        getHibernateTemplate().flush();
    }

    public void saveOrUpdateAll(Collection<T> entities) {
        getHibernateTemplate().saveOrUpdateAll(entities);
    }

    public void update(T entity) {
        getHibernateTemplate().merge(entity);
    }

    protected DetachedCriteria createDetachedCriteria() {
        return DetachedCriteria.forClass(persistenceClass);
    }

    protected <V> Criterion inRestriction(String name, Collection<V> values) {

        if (values.size() < MAX_IN_PARAMETER_SIZE) {
            return Restrictions.in(name, values);
        }

        List<V> listedValues = new ArrayList<V>(values);

        Disjunction disjunction = Restrictions.disjunction();
        for (int fromIndex = 0; fromIndex < listedValues.size();) {
            List<V> subSet;

            if (fromIndex + MAX_IN_PARAMETER_SIZE > listedValues.size()) {
                subSet = listedValues.subList(fromIndex, listedValues.size());
            } else {
                subSet = listedValues.subList(fromIndex, fromIndex + MAX_IN_PARAMETER_SIZE);
            }

            disjunction.add(Restrictions.in(name, subSet));
            fromIndex += subSet.size();
        }

        return disjunction;
    }
}
