package com.intuit.workcraft.servicecontractapp.controller;

import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import com.intuit.workcraft.servicecontractapp.model.ServiceContractStatus;
import com.intuit.workcraft.servicecontractapp.service.ContractMappingService;
import com.intuit.workcraft.servicecontractapp.service.ContractWorkerService;
import com.intuit.workcraft.servicecontractapp.service.ServiceContractService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ContractMappingControllerTest {

    @Mock
    private static ContractMappingService contractMappingService;

    @Mock
    private static ContractWorkerService contractWorkerService;

    @Mock
    private static ServiceContractService serviceContractService;

    @InjectMocks
    private static ContractMappingController contractMappingController;

    @BeforeAll
    public static void setUp() {
        contractMappingService = mock(ContractMappingService.class);
        serviceContractService = mock(ServiceContractService.class);
        contractWorkerService = mock(ContractWorkerService.class);
        contractMappingController = new ContractMappingController(contractMappingService, contractWorkerService, serviceContractService);
    }


    @Test
    public void testOnBoardWorkerSuccess() {
        // Mock data
        String employeeNumber = "123";
        String ownerId = "owner123";
        String serviceContractNumber = "ABC123";

        ContractWorker owner = new ContractWorker();
        owner.setId(123l);

        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber(employeeNumber);

        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber(serviceContractNumber);

        // Mock contractMappingService
        when(contractWorkerService.getContractWorker(ownerId)).thenReturn(owner);
        when(contractWorkerService.getContractWorker(employeeNumber)).thenReturn(contractWorker);
        when(serviceContractService.getServiceContract(serviceContractNumber)).thenReturn(serviceContract);
        when(contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractNumber)).thenReturn(null);
        when(contractMappingService.onboardWorker(contractWorker, serviceContract)).thenReturn(1234l);

        // Execute the method
        ResponseEntity<String> response = contractMappingController.onBoardWorker(employeeNumber, ownerId, serviceContractNumber, 100);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contract worker service contracts updated successfully.", response.getBody());
    }

    @Test
    public void testOnBoardWorkerValidationFailed() {
        // Mock data
        String employeeNumber = "123";
        String ownerId = "owner123";
        String serviceContractNumber = "ABC123";

        ContractWorker owner = new ContractWorker();
        owner.setId(123l);

        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber(employeeNumber);

        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber(serviceContractNumber);

        String validationMessage = "Validation failed.";

        // Mock contractMappingService
        when(contractWorkerService.getContractWorker(ownerId)).thenReturn(owner);
        when(contractWorkerService.getContractWorker(employeeNumber)).thenReturn(contractWorker);
        when(serviceContractService.getServiceContract(serviceContractNumber)).thenReturn(serviceContract);
        when(contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractNumber)).thenReturn(validationMessage);

        // Execute the method
        ResponseEntity<String> response = contractMappingController.onBoardWorker(employeeNumber, ownerId, serviceContractNumber, 100);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(validationMessage, response.getBody());

    }

    @Test
    public void testOffBoardWorkerSuccess() {
        // Mock data
        String employeeNumber = "123";
        String ownerId = "owner123";
        String serviceContractNumber = "ABC123";

        ContractWorker owner = new ContractWorker();
        owner.setId(123l);
        owner.setEmployeeNumber(ownerId);

        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber(employeeNumber);

        ServiceContract serviceContract = ServiceContract.builder().serviceContractStatus(ServiceContractStatus.ACTIVE.getStatus())
                .contractNumber(serviceContractNumber)
                .ownerId(ownerId)
                .owner(owner).build();

        // Mock contractMappingService
        when(contractWorkerService.getContractWorker(ownerId)).thenReturn(owner);
        when(contractWorkerService.getContractWorker(employeeNumber)).thenReturn(contractWorker);
        when(serviceContractService.getServiceContract(serviceContractNumber)).thenReturn(serviceContract);
        when(contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractNumber)).thenReturn(null);
        when(contractMappingService.offboardWorker(contractWorker, serviceContract)).thenReturn(1234l);

        // Execute the method
        ResponseEntity<String> response = contractMappingController.offBoardWorker(employeeNumber, ownerId, serviceContractNumber);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contract worker service contracts updated successfully.", response.getBody());

        // Verify the service method invocations
        verify(contractWorkerService, times(1)).getContractWorker(ownerId);
        verify(contractWorkerService, times(1)).getContractWorker(employeeNumber);
        verify(serviceContractService, times(1)).getServiceContract(serviceContractNumber);
        verify(contractMappingService, times(1)).performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractNumber);
        verify(contractMappingService, times(1)).offboardWorker(contractWorker, serviceContract);
    }

    @Test
    public void testOffBoardWorkerValidationFailed() {
        // Mock data
        String employeeNumber = "123";
        String ownerId = "owner123";
        String serviceContractNumber = "ABC123";

        ContractWorker owner = new ContractWorker();
        owner.setId(123l);

        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber(employeeNumber);

        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber(serviceContractNumber);

        String validationMessage = "Validation failed.";

        // Mock contractMappingService
        when(contractWorkerService.getContractWorker(ownerId)).thenReturn(owner);
        when(contractWorkerService.getContractWorker(employeeNumber)).thenReturn(contractWorker);
        when(serviceContractService.getServiceContract(serviceContractNumber)).thenReturn(serviceContract);
        when(contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractNumber))
                .thenReturn(validationMessage);

        // Execute the method
        ResponseEntity<String> response = contractMappingController.offBoardWorker(employeeNumber, ownerId, serviceContractNumber);

        // Verify the results
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(validationMessage, response.getBody());
    }

    @Test
    public void testOffBoardWorkerContractWorkerNotFound() {
        // Mock data
        String employeeNumber = "123";
        String ownerId = "owner123";
        String serviceContractNumber = "ABC123";

        ContractWorker owner = new ContractWorker();
        owner.setId(123l);

        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber(serviceContractNumber);

        // Mock contractWorkerService to return null for contractWorker
        when(contractWorkerService.getContractWorker(ownerId)).thenReturn(owner);
        when(contractWorkerService.getContractWorker(employeeNumber)).thenReturn(null);
        when(serviceContractService.getServiceContract(serviceContractNumber)).thenReturn(serviceContract);
        when(contractMappingService.performValidations(owner, null, serviceContract, ownerId, employeeNumber, serviceContractNumber))
                .thenReturn("Contract worker 123 not onboarded to Service Contract ABC123.");
        // Execute the method
        ResponseEntity<String> response = contractMappingController.offBoardWorker(employeeNumber, ownerId, serviceContractNumber);

        // Verify the results
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Contract worker 123 not onboarded to Service Contract ABC123.", response.getBody());

    }

    @Test
    public void onBoardWorkerValidDataAndAllocationNullShouldReturnSuccessResponse() {
        // Arrange
        String employeeNumber = "E001";
        String ownerId = "O001";
        String serviceContractNumber = "SC001";
        Integer allocation = null;
        ContractWorker owner = ContractWorker.builder().employeeNumber(ownerId).build();
        ContractWorker contractWorker =  ContractWorker.builder().employeeNumber(employeeNumber).build();
        ServiceContract serviceContract = ServiceContract.builder().contractNumber(serviceContractNumber).build();
        Mockito.when(contractWorkerService.getContractWorker(ownerId)).thenReturn(owner);
        Mockito.when(contractWorkerService.getContractWorker(employeeNumber)).thenReturn(contractWorker);
        Mockito.when(serviceContractService.getServiceContract(serviceContractNumber)).thenReturn(serviceContract);
        Mockito.when(contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractNumber))
                .thenReturn(null);
        Mockito.when(contractMappingService.onboardWorker(contractWorker, serviceContract)).thenReturn(1L);


        ResponseEntity<String> response = contractMappingController.onBoardWorker(employeeNumber, ownerId, serviceContractNumber, allocation);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contract worker service contracts updated successfully.", response.getBody());
    }

    @Test
    public void onBoardWorkerValidDataAndAllocation100ShouldReturnSuccessResponse() {
        String employeeNumber = "E001";
        String ownerId = "O001";
        String serviceContractNumber = "SC001";
        Integer allocation = 100;
        ContractWorker owner = ContractWorker.builder().employeeNumber(ownerId).build();
        ContractWorker contractWorker =  ContractWorker.builder().employeeNumber(employeeNumber).build();
        ServiceContract serviceContract = ServiceContract.builder().contractNumber(serviceContractNumber).build();

        when(contractWorkerService.getContractWorker(ownerId)).thenReturn(owner);
        when(contractWorkerService.getContractWorker(employeeNumber)).thenReturn(contractWorker);
        when(serviceContractService.getServiceContract(serviceContractNumber)).thenReturn(serviceContract);
        when(contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractNumber))
                .thenReturn(null);
        when(contractMappingService.onboardWorker(contractWorker, serviceContract)).thenReturn(1L);


        ResponseEntity<String> response = contractMappingController.onBoardWorker(employeeNumber, ownerId, serviceContractNumber, allocation);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contract worker service contracts updated successfully.", response.getBody());
        verify(contractMappingService, Mockito.times(1)).onboardWorker(contractWorker, serviceContract);
    }

    @Test
    public void onBoardWorkerValidDataAndAllocationNot100ShouldReturnBadRequestResponse() {

        String employeeNumber = "E001";
        String ownerId = "O001";
        String serviceContractNumber = "SC001";
        Integer allocation = 50;

        ResponseEntity<String> response = contractMappingController.onBoardWorker(employeeNumber, ownerId, serviceContractNumber, allocation);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Contract worker must have 100% allocation.", response.getBody());
    }


}
