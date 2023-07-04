package com.intuit.workcraft.servicecontractapp.repository;

import com.intuit.workcraft.servicecontractapp.model.ContractMapping;
import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractMappingRepository extends JpaRepository<ContractMapping, Long> {

    ContractMapping findContractMappingByContractWorkerAndServiceContract(ContractWorker contractWorker, ServiceContract serviceContract);
}
