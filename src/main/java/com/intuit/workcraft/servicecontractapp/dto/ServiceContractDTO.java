package com.intuit.workcraft.servicecontractapp.dto;
import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class ServiceContractDTO {

    private String contractNumber;
    private String serviceContractStatus;
    private String ownerId;
    private List<WorkerLite> workerAssigned;

}
