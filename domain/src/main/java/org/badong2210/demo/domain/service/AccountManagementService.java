package org.badong2210.demo.domain.service;

import org.badong2210.demo.domain.vo.AccountVO;

import java.util.List;

public interface AccountManagementService {
    List<AccountVO> getSessionAccounts(String sessionId);

    int activate(String sessionId, List<String> accountNames);

    int archive(String sessionId, List<String> accountNames);

    int delete(String sessionId, List<String> accountNames);

    AccountVO addAccount(AccountVO account);
}
