package com.intuit.workcraft.servicecontractapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkerLite {

    private String employeeNumber;
    private int allocation;
}
