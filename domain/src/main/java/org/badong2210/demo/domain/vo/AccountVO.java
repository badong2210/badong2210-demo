package org.badong2210.demo.domain.vo;

import org.badong2210.core.vo.AuditedVO;

import java.util.Date;

public class AccountVO extends AuditedVO {
    private String name;

    private String sessionID;

    private String description;

    private String status;

    private Date activationTime;

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

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AccountVO{" +
                "activationCode='" + activationCode + '\'' +
                ", name='" + name + '\'' +
                ", sessionID='" + sessionID + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", activationTime=" + activationTime +
                '}';
    }
}
