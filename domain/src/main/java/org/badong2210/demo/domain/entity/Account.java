package org.badong2210.demo.domain.entity;

import org.badong2210.core.entity.BaseAuditedBean;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "account")
public class Account extends BaseAuditedBean {

    private static final long serialVersionUID = 1L;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name="session_id", length = 255, nullable = false)
    private String sessionID;

    @Column(columnDefinition = "text", nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AccountStatus status = AccountStatus.INACTIVE;

    @Column(name = "activation_time", nullable = true)
    private Date activationTime;

    @Column(name = "activation_code", length = 50, nullable = false)
    private String activationCode;

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Date getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Date activationTime) {
        this.activationTime = activationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
