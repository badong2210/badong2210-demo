package org.badong2210.demo.controller.account;

public enum StatusResponse {

    SUCCESS, FAILED;

    public String getStatus() {
        return name();
    }
}
