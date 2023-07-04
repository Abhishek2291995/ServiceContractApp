package com.intuit.workcraft.servicecontractapp.controller;

import com.intuit.workcraft.servicecontractapp.dto.ContractWorkerDTO;
import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.service.ContractWorkerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContractWorkerControllerTest {

    @Mock
    private static ContractWorkerService contractWorkerService;

    @InjectMocks
    private static ContractWorkerController contractWorkerController;

    @BeforeAll
    public static void setUp() {
        contractWorkerService = mock(ContractWorkerService.class);
        contractWorkerController = new ContractWorkerController(contractWorkerService);
    }


    @Test
    public void testSaveContractWorkerSuccess() {
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber("123");

        when(contractWorkerService.saveContractWorker(any(ContractWorker.class))).thenReturn(contractWorker);

        ResponseEntity response = contractWorkerController.saveContractWorker(contractWorker);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Contract worker onboarded successfully.", response.getBody());
    }

    @Test
    public void testSaveContractWorkerContractWorkerAlreadyPresent() {
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber("123");

        when(contractWorkerService.saveContractWorker(contractWorker)).thenReturn(null);

        ResponseEntity response = contractWorkerController.saveContractWorker(contractWorker);

        assertEquals(HttpStatus.ALREADY_REPORTED, response.getStatusCode());
        assertEquals("Worker with employee Number 123 already present", response.getBody());
    }

    @Test
    public void testGetControllerWorkerExistingContractWorker() {
        String employeeNumber = "123";
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber(employeeNumber);

        when(contractWorkerService.getContractWorker(employeeNumber)).thenReturn(contractWorker);

        ResponseEntity<ContractWorkerDTO> response = contractWorkerController.getControllerWorker(employeeNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(contractWorker.getEmployeeNumber(), response.getBody().getEmployeeNumber());
    }

    @Test
    public void testGetControllerWorkerNonExistingContractWorker() {
        String employeeNumber = "123";

        when(contractWorkerService.getContractWorker(employeeNumber)).thenReturn(null);

        ResponseEntity<ContractWorkerDTO> response = contractWorkerController.getControllerWorker(employeeNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetAllWorkersExistingContractWorkers() {
        List<ContractWorker> contractWorkers = new ArrayList<>();
        ContractWorker contractWorker1 = new ContractWorker();
        contractWorker1.setEmployeeNumber("123");
        ContractWorker contractWorker2 = new ContractWorker();
        contractWorker2.setEmployeeNumber("456");
        contractWorkers.add(contractWorker1);
        contractWorkers.add(contractWorker2);

        when(contractWorkerService.getAllWorkers()).thenReturn(contractWorkers);

        ResponseEntity<List<ContractWorkerDTO>> response = contractWorkerController.getAllWorkers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(contractWorkers.size(), response.getBody().size());
        assertEquals(contractWorkers.get(0).getEmployeeNumber(), response.getBody().get(0).getEmployeeNumber());
        assertEquals(contractWorkers.get(1).getEmployeeNumber(), response.getBody().get(1).getEmployeeNumber());
    }

    @Test
    public void testGetAllWorkersNoContractWorkers() {
        when(contractWorkerService.getAllWorkers()).thenReturn(null);

        ResponseEntity<List<ContractWorkerDTO>> response = contractWorkerController.getAllWorkers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
