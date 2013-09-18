package org.badong2210.demo.domain.dao;

import org.badong2210.demo.domain.entity.Account;
import org.badong2210.demo.domain.entity.AccountStatus;
import org.badong2210.core.dao.BaseDao;

import java.util.List;

public interface AccountDao extends BaseDao<Account> {

    List<Account> getSessionAccounts(String sessionId);

    int changeStatus(String sessionId, List<String> names, AccountStatus status);

    int delete(final String sessionId, final List<String> names);
}
