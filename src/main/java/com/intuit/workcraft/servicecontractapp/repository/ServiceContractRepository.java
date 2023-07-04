package com.intuit.workcraft.servicecontractapp.repository;

import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceContractRepository extends JpaRepository<ServiceContract, Long> {

    ServiceContract getServiceContractByContractNumber(String contractNumber);
}
