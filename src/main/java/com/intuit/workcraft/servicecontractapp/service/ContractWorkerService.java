package com.intuit.workcraft.servicecontractapp.service;

import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.repository.ContractWorkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContractWorkerService {

    final private ContractWorkerRepository contractWorkerRepository;

    @Autowired
    ContractWorkerService(ContractWorkerRepository contractWorkerRepository) {
        this.contractWorkerRepository = contractWorkerRepository;
    }
    public ContractWorker saveContractWorker(ContractWorker contractWorker) {
        if (getContractWorker(contractWorker.getEmployeeNumber()) != null) {
            return null;
        }
        return contractWorkerRepository.save(contractWorker);
    }

    public ContractWorker getContractWorker(String employeeNumber) {
        return contractWorkerRepository.findContractWorkerByEmployeeNumber(employeeNumber);
    }

    public List<ContractWorker> getAllWorkers(){
        return contractWorkerRepository.findAll();
    }
}
