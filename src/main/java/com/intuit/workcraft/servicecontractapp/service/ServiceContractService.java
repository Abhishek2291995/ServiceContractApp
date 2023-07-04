package com.intuit.workcraft.servicecontractapp.service;

import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import com.intuit.workcraft.servicecontractapp.repository.ServiceContractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ServiceContractService {

    final private ServiceContractRepository serviceContractRepository;

    @Autowired
    public ServiceContractService(ServiceContractRepository serviceContractRepository) {
        this.serviceContractRepository = serviceContractRepository;
    }

    public ServiceContract saveServiceContract(ServiceContract serviceContract) {
        if (getServiceContract(serviceContract.getContractNumber()) != null) {
            log.debug("Service contract already present");
            return null;
        }
        return serviceContractRepository.save(serviceContract);
    }

    public ServiceContract getServiceContract(String contractNumber) {
        return serviceContractRepository.getServiceContractByContractNumber(contractNumber);
    }

    public List<ServiceContract> getAllServiceContracts() {
        return serviceContractRepository.findAll();
    }

    public ServiceContract updateServiceContract(ServiceContract serviceContract) {
        return serviceContractRepository.saveAndFlush(serviceContract);
    }
}
