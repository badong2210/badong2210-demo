package org.badong2210.demo.domain.vo;

import org.badong2210.demo.domain.entity.AccountStatus;

import java.util.Date;
import java.util.UUID;

public class AccountVOBuilder {

    private AccountVO account = new AccountVO();

    public AccountVOBuilder() {
        this(new AccountVO());
    }

    public AccountVOBuilder(AccountVO vo) {
        account.setName(vo.getName());
        account.setDescription(vo.getDescription());
        account.setStatus(AccountStatus.INACTIVE.name());
        account.setCreatedTime(new Date());
        account.setCreatedBy("builder");
        account.setLastUpdatedBy("builder");
        account.setLastUpdatedTime(new Date());
        account.setActivationCode(String.valueOf(UUID.randomUUID().hashCode()));
    }

    public AccountVOBuilder setUser(String user) {
        account.setCreatedBy(user);
        account.setLastUpdatedBy(user);

        return this;
    }
    public AccountVOBuilder setName(String name) {
        account.setName(name);

        return this;
    }

    public AccountVOBuilder setDescription(String desc) {
        account.setDescription(desc);

        return this;
    }

    public AccountVOBuilder setSessionID(String sessionID) {
        account.setSessionID(sessionID);

        return this;
    }

    public AccountVO build() {
        return account;
    }
}
