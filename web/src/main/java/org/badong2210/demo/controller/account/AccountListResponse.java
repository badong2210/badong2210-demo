package org.badong2210.demo.controller.account;

import java.util.List;

public class AccountListResponse {

    private StatusResponse status;

    private List<AccountRow> rows;

    public List<AccountRow> getRows() {
        return rows;
    }

    public void setRows(List<AccountRow> rows) {
        this.rows = rows;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }
}
