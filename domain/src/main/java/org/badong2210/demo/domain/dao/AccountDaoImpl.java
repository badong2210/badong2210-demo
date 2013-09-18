package org.badong2210.demo.domain.dao;

import org.badong2210.core.dao.BaseDaoImpl;
import org.badong2210.demo.domain.entity.Account;
import org.badong2210.demo.domain.entity.AccountStatus;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.List;

public class AccountDaoImpl extends BaseDaoImpl<Account> implements AccountDao {
    public AccountDaoImpl() {
        super(Account.class);
    }

    @SuppressWarnings("unchecked")
    public List<Account> getSessionAccounts(String sessionId) {
        DetachedCriteria criteria = createDetachedCriteria();
        criteria.add(Restrictions.eq("sessionID", sessionId));
        criteria.addOrder(Order.asc("name"));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    public int changeStatus(final String sessionId, final List<String> names, final AccountStatus status) {
        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("update account set status=:status where session_id=:sessionId and name in (:names)");

                query.setString("status", status.name());
                query.setParameterList("names", names);
                query.setString("sessionId", sessionId);

                return query.executeUpdate();
            }
        });
    }

    public int delete(final String sessionId, final List<String> names) {
        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("delete from account where session_id=:sessionId and name in (:names)");

                query.setParameterList("names", names);
                query.setString("sessionId", sessionId);

                return query.executeUpdate();
            }
        });
    }
}
