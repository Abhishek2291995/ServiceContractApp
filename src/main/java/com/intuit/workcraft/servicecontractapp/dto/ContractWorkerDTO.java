package com.intuit.workcraft.servicecontractapp.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class ContractWorkerDTO {

    private String employeeNumber;
    private String firstName;
    private String lastName;
    private List<String> serviceContractAssigned;
    private LocalDate startDate;
    private String role;
}

