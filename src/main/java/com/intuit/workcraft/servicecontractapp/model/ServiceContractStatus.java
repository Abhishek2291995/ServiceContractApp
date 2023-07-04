package com.intuit.workcraft.servicecontractapp.model;

public enum ServiceContractStatus {

    DRAFT("DRAFT"),
    APPROVED("APPROVED"),
    INACTIVE("INACTIVE"),
    ACTIVE("ACTIVE");

    private String status;
    ServiceContractStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

}
