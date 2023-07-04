package com.intuit.workcraft.servicecontractapp.controller;

import com.intuit.workcraft.servicecontractapp.dto.ServiceContractDTO;
import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import com.intuit.workcraft.servicecontractapp.service.ContractWorkerService;
import com.intuit.workcraft.servicecontractapp.service.ServiceContractService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ServiceContractControllerTest {

    @Mock
    private static ServiceContractService serviceContractService;

    @Mock
    private static ContractWorkerService contractWorkerService;

    @InjectMocks
    private static ServiceContractController serviceContractController;



    @BeforeAll
    public static void setUp() {
        contractWorkerService = mock(ContractWorkerService.class);
        serviceContractService = mock(ServiceContractService.class);
        serviceContractController = new ServiceContractController(serviceContractService,contractWorkerService);
    }


    @Test
    public void testSaveServiceContractSuccess() {

        // Mock data
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber("12345");
        serviceContract.setOwnerId("owner123");

        ContractWorker owner = new ContractWorker();
        // Mock serviceContractService
        when(contractWorkerService.getContractWorker("owner123")).thenReturn(owner);
        when(serviceContractService.saveServiceContract(serviceContract)).thenReturn(serviceContract);

        ResponseEntity<String> response = serviceContractController.saveServiceContract(serviceContract);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Service contract created successfully.", response.getBody());

        // Verify the service method invocations
        verify(contractWorkerService, times(1)).getContractWorker("owner123");
        verify(serviceContractService, times(1)).saveServiceContract(serviceContract);
        verifyNoMoreInteractions(contractWorkerService, serviceContractService);
    }

    @Test
    public void testSaveServiceContractOwnerNotPresent() {
        // Mock data
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber("12345");
        serviceContract.setOwnerId("owner123");

        // Mock serviceContractService
        when(contractWorkerService.getContractWorker("owner123")).thenReturn(null);

        ResponseEntity<String> response = serviceContractController.saveServiceContract(serviceContract);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Contract Owner with owner id owner123 not present.", response.getBody());

        // Verify the service method invocations
        verify(contractWorkerService, times(2)).getContractWorker("owner123");
    }


    @Test
    public void testGetServiceContractSuccess() {
        // Mock data
        String contractNumber = "12345";
        ServiceContract serviceContract = ServiceContract.builder()
                .owner(ContractWorker.builder().build())
                .contractMappings(Collections.emptyList()).build();

        // Mock serviceContractService
        when(serviceContractService.getServiceContract(contractNumber)).thenReturn(serviceContract);

        ResponseEntity<ServiceContractDTO> response = serviceContractController.getServiceContract(contractNumber);

        ServiceContractDTO serviceContractDTO = ServiceContractDTO.builder().workerAssigned(Collections.emptyList()).build();
        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(serviceContractDTO, response.getBody());

        // Verify the service method invocations
        verify(serviceContractService, times(2)).getServiceContract(contractNumber);
        verifyNoMoreInteractions(serviceContractService);
    }

    @Test
    public void testChangeServiceContractOwnerSuccess() {
        // Mock data
        String ownerId = "123";
        String contractNumber = "12345";
        ContractWorker owner = new ContractWorker();
        owner.setId(123l);
        ServiceContract serviceContract = new ServiceContract();

        // Mock contractWorkerService
        when(contractWorkerService.getContractWorker(ownerId)).thenReturn(owner);
        // Mock serviceContractService
        when(serviceContractService.getServiceContract(contractNumber)).thenReturn(serviceContract);
        when(serviceContractService.updateServiceContract(serviceContract)).thenReturn(serviceContract);

        // Execute the method
        ResponseEntity<String> response = serviceContractController.changeServiceContractOwner(ownerId, contractNumber);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Owner changed successfully", response.getBody());

        // Verify the service method invocations
        verify(contractWorkerService, times(1)).getContractWorker(ownerId);
        verify(serviceContractService, times(1)).getServiceContract(contractNumber);
        verify(serviceContractService, times(1)).updateServiceContract(serviceContract);
    }

    @Test
    public void testGetAllServiceContractsSuccess() {
        // Mock data
        List<ServiceContract> serviceContractList = new ArrayList<>();
        serviceContractList.add(ServiceContract.builder()
                .owner(ContractWorker.builder().build())
                .contractMappings(Collections.emptyList()).build());
        serviceContractList.add(ServiceContract.builder().owner(ContractWorker.builder().build())
                .contractMappings(Collections.emptyList())
                .build());

        // Mock serviceContractService
        when(serviceContractService.getAllServiceContracts()).thenReturn(serviceContractList);

        ResponseEntity<List<ServiceContractDTO>> response = serviceContractController.getAllWorkers();

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(serviceContractList.size(), response.getBody().size());

        // Verify the service method invocations
        verify(serviceContractService, times(1)).getAllServiceContracts();
    }
}
