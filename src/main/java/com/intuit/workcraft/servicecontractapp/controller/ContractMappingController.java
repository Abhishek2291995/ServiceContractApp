package com.intuit.workcraft.servicecontractapp.controller;

import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import com.intuit.workcraft.servicecontractapp.service.ContractMappingService;
import com.intuit.workcraft.servicecontractapp.service.ContractWorkerService;
import com.intuit.workcraft.servicecontractapp.service.ServiceContractService;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contract-mappings")
@Slf4j
public class ContractMappingController {

    private final ContractMappingService contractMappingService;
    private final ContractWorkerService contractWorkerService;
    private final ServiceContractService serviceContractService;


    @Autowired
    public ContractMappingController(ContractMappingService contractMappingService,
                                     ContractWorkerService contractWorkerService,
                                     ServiceContractService serviceContractService) {
        this.contractMappingService = contractMappingService;
        this.contractWorkerService = contractWorkerService;
        this.serviceContractService = serviceContractService;
    }

    @PostMapping("/onBoard")
    public ResponseEntity<String> onBoardWorker(@RequestParam @NotNull String employeeNumber,
                                                @RequestParam @NotNull String ownerId,
                                                @RequestParam @NotNull String serviceContractNumber,
                                                @RequestParam(required = false) Integer allocation) {

        if (allocation != null && allocation != 100) {
            log.debug("Allocation is not 100");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Contract worker must have 100% allocation.");
        }

        ContractWorker owner = contractWorkerService.getContractWorker(ownerId);
        ContractWorker contractWorker = contractWorkerService.getContractWorker(employeeNumber);
        ServiceContract serviceContract = serviceContractService.getServiceContract(serviceContractNumber);

        String validationMessage = contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractNumber);
        if (validationMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationMessage);
        }
        log.info("Validation successful");
        if(contractMappingService.onboardWorker(contractWorker, serviceContract) != null) {
            return ResponseEntity.ok("Contract worker service contracts updated successfully.");
        }
        log.debug("Something went wrong while on-boarding the Contract worker");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong while on-boarding the worker");
    }

    @PostMapping("/offBoard")
    public ResponseEntity<String> offBoardWorker(@RequestParam @NotNull String employeeNumber,
                                                 @RequestParam @NotNull String ownerId,
                                                 @RequestParam @NotNull String serviceContractNumber) {

        ContractWorker owner = contractWorkerService.getContractWorker(ownerId);
        ContractWorker contractWorker = contractWorkerService.getContractWorker(employeeNumber);
        ServiceContract serviceContract = serviceContractService.getServiceContract(serviceContractNumber);

        String validationMessage = contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractNumber);
        if (validationMessage != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(validationMessage);
        }
        log.info("Validation successful");
        if (contractMappingService.offboardWorker(contractWorker, serviceContract) != null) {
            return ResponseEntity.ok("Contract worker service contracts updated successfully.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(String.format("Contract worker %s not onboarded to Service Contract %s .",contractWorker.getEmployeeNumber(),
                        serviceContract.getContractNumber()));
    }
}