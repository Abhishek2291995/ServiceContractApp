package com.intuit.workcraft.servicecontractapp.controller;
import com.intuit.workcraft.servicecontractapp.dto.ServiceContractDTO;
import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import com.intuit.workcraft.servicecontractapp.service.ContractWorkerService;
import com.intuit.workcraft.servicecontractapp.service.ServiceContractService;
import com.intuit.workcraft.servicecontractapp.utility.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/service-contracts")
public class ServiceContractController {

    private final ServiceContractService serviceContractService;
    private final ContractWorkerService contractWorkerService;

    @Autowired
    public ServiceContractController(ServiceContractService serviceContractService, ContractWorkerService contractWorkerService) {
        this.serviceContractService = serviceContractService;
        this.contractWorkerService = contractWorkerService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveServiceContract(@RequestBody ServiceContract serviceContract) {


        ContractWorker owner = contractWorkerService.getContractWorker(serviceContract.getOwnerId());
        if (owner == null) {
            log.debug("Owner not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Contract Owner with owner id %s not present.", serviceContract.getOwnerId()));
        }
        serviceContract.setOwner(owner);
        if(serviceContractService.saveServiceContract(serviceContract) != null) {
            log.info("Service Contract saved successfully");
            return ResponseEntity.ok("Service contract created successfully.");
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(String.format("Contract Service with contract Number " +
                "%s already present", serviceContract.getContractNumber()));
    }

    @GetMapping("/{contractNumber}")
    public ResponseEntity<ServiceContractDTO> getServiceContract(@PathVariable String contractNumber) {
        return Optional.ofNullable(serviceContractService.getServiceContract(contractNumber))
                .map(serviceContract ->  DtoMapper.serviceContractDTO(serviceContract))
                .map(ResponseEntity::ok)
                .orElseGet(() ->  ResponseEntity.notFound().build());
    }

    @GetMapping("/allServiceContracts")
    public ResponseEntity<List<ServiceContractDTO>> getAllWorkers() {
        return Optional.ofNullable(serviceContractService.getAllServiceContracts())
                .map(serviceContractList -> serviceContractList.stream().map(serviceContract -> DtoMapper.serviceContractDTO(serviceContract))
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/changeOwner")
    public ResponseEntity<String> changeServiceContractOwner(@RequestParam String ownerId, @RequestParam String contractNumber) {

        ContractWorker owner = contractWorkerService.getContractWorker(ownerId);
        if (owner == null) {
            log.debug("Owner not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Contract Owner with owner id %s not present.", ownerId));
        }

        ServiceContract serviceContract = serviceContractService.getServiceContract(contractNumber);
        if (serviceContract == null) {
            log.debug("ServiceContract not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Contract Service with contract Number %s not present.", contractNumber));
        }
        serviceContract.setOwner(owner);
        if(serviceContractService.updateServiceContract(serviceContract) != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Owner changed successfully");
        }
        log.debug("Error while changing owner");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
