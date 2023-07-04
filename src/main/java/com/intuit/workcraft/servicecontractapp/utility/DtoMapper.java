package com.intuit.workcraft.servicecontractapp.utility;

import com.intuit.workcraft.servicecontractapp.dto.ContractWorkerDTO;
import com.intuit.workcraft.servicecontractapp.dto.ServiceContractDTO;
import com.intuit.workcraft.servicecontractapp.dto.WorkerLite;
import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.model.ServiceContract;

import java.util.stream.Collectors;

public class DtoMapper {
    /**
     * Mapper method
     * @param contractWorker
     * @return
     */
    public static ContractWorkerDTO contractWorkerDTOMapper(ContractWorker contractWorker) {
        return ContractWorkerDTO.builder().startDate(contractWorker.getStartDate())
                        .firstName(contractWorker.getFirstName())
                        .lastName(contractWorker.getLastName())
                        .employeeNumber(contractWorker.getEmployeeNumber())
                        .role(contractWorker.getRole())
                        .serviceContractAssigned(contractWorker.getContractMappings().stream()
                                .map(contractMapping -> contractMapping.getServiceContract().
                                        getContractNumber()).collect(Collectors.toList())).build();
    }

    /**
     *
     * @param serviceContract
     * @return
     */
    public static ServiceContractDTO serviceContractDTO(ServiceContract serviceContract) {
        return ServiceContractDTO.builder().contractNumber(serviceContract.getContractNumber())
                .serviceContractStatus(serviceContract.getServiceContractStatus())
                .ownerId(serviceContract.getOwner().getEmployeeNumber())
                .workerAssigned(serviceContract.getContractMappings().stream().map(contractMapping ->
                        WorkerLite.builder()
                                .employeeNumber(contractMapping.getContractWorker().getEmployeeNumber())
                                .allocation(contractMapping.getAllocation())
                                .build()).collect(Collectors.toList()))
                .build();
    }
}
