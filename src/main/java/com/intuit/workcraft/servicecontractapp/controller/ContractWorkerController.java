package com.intuit.workcraft.servicecontractapp.controller;

import com.intuit.workcraft.servicecontractapp.dto.ContractWorkerDTO;
import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.service.ContractWorkerService;
import com.intuit.workcraft.servicecontractapp.utility.DtoMapper;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/contract-workers")
public class ContractWorkerController {

    private final ContractWorkerService contractWorkerService;

    @Autowired
    public ContractWorkerController(ContractWorkerService contractWorkerService) {
        this.contractWorkerService = contractWorkerService;
    }

    @PostMapping("/saveContractWorker")
    public ResponseEntity saveContractWorker(@RequestBody @NotNull ContractWorker contractWorker) {

        if (contractWorkerService.saveContractWorker(contractWorker) != null) {
            log.info("ContractWorker created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body("Contract worker onboarded successfully.");
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(String.format("Worker with employee Number " +
                "%s already present", contractWorker.getEmployeeNumber()));
    }

    @GetMapping("/{employeeNumber}")
    public ResponseEntity<ContractWorkerDTO> getControllerWorker(@PathVariable @NotNull String employeeNumber) {
        return Optional.ofNullable(contractWorkerService.getContractWorker(employeeNumber))
                .map(contractWorker ->  DtoMapper.contractWorkerDTOMapper(contractWorker))
                .map(ResponseEntity::ok)
                .orElseGet(() ->  ResponseEntity.notFound().build());
    }

    @GetMapping("/allWorkers")
    public ResponseEntity<List<ContractWorkerDTO>> getAllWorkers() {
        return Optional.ofNullable(contractWorkerService.getAllWorkers())
                .map(contractWorkers -> contractWorkers.stream().map(contractWorker -> DtoMapper.contractWorkerDTOMapper(contractWorker))
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
