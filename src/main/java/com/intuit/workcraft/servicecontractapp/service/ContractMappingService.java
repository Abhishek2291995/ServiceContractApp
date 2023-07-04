package com.intuit.workcraft.servicecontractapp.service;

import com.intuit.workcraft.servicecontractapp.model.ContractMapping;
import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import com.intuit.workcraft.servicecontractapp.model.ServiceContractStatus;
import com.intuit.workcraft.servicecontractapp.repository.ContractMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContractMappingService {

    private final ContractMappingRepository contractMappingRepository;


    @Autowired
    public ContractMappingService(ContractMappingRepository contractMappingRepository) {
        this.contractMappingRepository = contractMappingRepository;
    }

    public Long onboardWorker(ContractWorker contractWorker, ServiceContract serviceContract) {

        ContractMapping contractMapping = contractMappingRepository
                .findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract);
        if (contractMapping == null) {
            contractMapping = contractMappingRepository.save(ContractMapping.builder()
                    .contractWorker(contractWorker).serviceContract(serviceContract).build());
        }
        log.info("Contract Worker onboarded successfully");
        return contractMapping.getId();
    }

    public Long offboardWorker(ContractWorker contractWorker, ServiceContract serviceContract) {
        ContractMapping contractMapping = contractMappingRepository
                .findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract);

        if (contractMapping != null) {
            contractMappingRepository.deleteById(contractMapping.getId());
            log.info("Contract Worker off-boarded successfully");
            return contractMapping.getId();
        }
        log.debug("Contract Worker not onboarded to service contract");
        return null;
    }


    public String performValidations(ContractWorker owner, ContractWorker contractWorker,
                                     ServiceContract serviceContract, String ownerId,
                                     String employeeNumber, String serviceContractId) {
        if (owner == null ) {
            return String.format("No owner with owner id %s found." , ownerId);
        }

        if (contractWorker == null ) {
            return String.format("No worker with employeeNumber %s found." , employeeNumber);
        }

        if (employeeNumber != null && ownerId != null  && employeeNumber.equals(ownerId)) {
            return "OwnerId is same as employee Number";
        }

        if (serviceContract == null) {
            return String.format("Service Contract with contract Number %s Doesn't exist", serviceContractId);
        }
        // Check if the authenticated user is the owner
        if (serviceContract.getOwner().getId() != owner.getId()) {
            return "Only the owner can onboard a contract worker.";
        }

        if (!ServiceContractStatus.ACTIVE.getStatus().equalsIgnoreCase(serviceContract.getServiceContractStatus()) ) {
            return String.format("Service Contract status is not active, Current Status: %s",
                    serviceContract.getServiceContractStatus());
        }
        return null;
    }
}
