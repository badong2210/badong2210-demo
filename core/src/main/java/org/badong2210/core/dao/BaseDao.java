package org.badong2210.core.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BaseDao<T extends Serializable> {

    HibernateTemplate getHibernateTemplate();
    void setHibernateTemplate(HibernateTemplate hibernateTemplate);
    JdbcTemplate getJdbcTemplate();
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    void forceVersionUpdate(T entity);
    void save(T entity);
    void saveAndFlush(T entity);
    void flush();
    void delete(T entity);
    void update(T entity);
    void saveOrUpdateAll(Collection<T> entities);
    void evict(T entity);
    T get(Serializable key);
    List<T> getAll();
}
