package com.intuit.workcraft.servicecontractapp.repository;

import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractWorkerRepository extends JpaRepository<ContractWorker, Long> {

    ContractWorker findContractWorkerByEmployeeNumber(String employeeNumber);

}
