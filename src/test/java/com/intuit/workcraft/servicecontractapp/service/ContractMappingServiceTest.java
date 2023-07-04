package com.intuit.workcraft.servicecontractapp.service;

import com.intuit.workcraft.servicecontractapp.model.ContractMapping;
import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import com.intuit.workcraft.servicecontractapp.model.ServiceContractStatus;
import com.intuit.workcraft.servicecontractapp.repository.ContractMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContractMappingServiceTest {

    @Mock
    private ContractMappingRepository contractMappingRepository;

    @InjectMocks
    private ContractMappingService contractMappingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnboardWorkerValidInputSuccess() {
        ContractWorker contractWorker = ContractWorker.builder().id(1l)
                .firstName("Abc")
                .lastName("xyz")
                .employeeNumber("emp1")
                .role("contract")
                .build();
        ServiceContract serviceContract = ServiceContract.builder()
                .id(1l)
                .serviceContractStatus(ServiceContractStatus.ACTIVE.getStatus())
                .contractNumber("c1")
                .ownerId("own1").build();

        when(contractMappingRepository.findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract))
                .thenReturn(null);
        when(contractMappingRepository.save(any(ContractMapping.class)))
                .thenReturn(ContractMapping.builder().id(1L).contractWorker(contractWorker).serviceContract(serviceContract).build());

        Long mappingId = contractMappingService.onboardWorker(contractWorker, serviceContract);

        assertNotNull(mappingId);
        assertEquals(1L, mappingId);
        verify(contractMappingRepository, times(1)).findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract);
        verify(contractMappingRepository, times(1)).save(any(ContractMapping.class));
    }

    @Test
    public void testOnboardWorkerAlreadyOnboardedReturnsNull() {
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setId(1L);
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setId(1L);
        ContractMapping existingMapping = ContractMapping.builder().id(1L).contractWorker(contractWorker).serviceContract(serviceContract).build();

        when(contractMappingRepository.findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract))
                .thenReturn(existingMapping);

        Long mappingId = contractMappingService.onboardWorker(contractWorker, serviceContract);

        assertNotNull(mappingId);
        verify(contractMappingRepository, times(1)).findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract);
        verify(contractMappingRepository, times(0)).save(any(ContractMapping.class));
    }

    @Test
    public void testOffboardWorkerValidInputSuccess() {

        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setId(1L);
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setId(1L);
        ContractMapping existingMapping = ContractMapping.builder().id(1L).contractWorker(contractWorker).serviceContract(serviceContract).build();

        when(contractMappingRepository.findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract))
                .thenReturn(existingMapping);

        Long mappingId = contractMappingService.offboardWorker(contractWorker, serviceContract);

        assertNotNull(mappingId);
        assertEquals(1L, mappingId);
        verify(contractMappingRepository, times(1)).findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract);
        verify(contractMappingRepository, times(1)).deleteById(existingMapping.getId());
    }

    @Test
    public void testOffboardWorkerNotOnboardedReturnsNull() {
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setId(1L);
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setId(1L);

        when(contractMappingRepository.findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract))
                .thenReturn(null);

        Long mappingId = contractMappingService.offboardWorker(contractWorker, serviceContract);

        assertNull(mappingId);
        verify(contractMappingRepository, times(1)).findContractMappingByContractWorkerAndServiceContract(contractWorker, serviceContract);
        verify(contractMappingRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void testPerformValidationsValidInputReturnsNull() {
        ContractWorker owner = new ContractWorker();
        owner.setId(1L);
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setId(2L);
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setId(3L);
        String ownerId = "1";
        String employeeNumber = "2";
        String serviceContractId = "3";
        serviceContract.setOwner(owner);
        serviceContract.setServiceContractStatus(ServiceContractStatus.ACTIVE.getStatus());

        String validationMessage = contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractId);

        assertNull(validationMessage);
    }

    @Test
    public void testPerformValidationsNullOwnerReturnsErrorMessage() {
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setId(2L);
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setId(3L);
        String ownerId = "1";
        String employeeNumber = "2";
        String serviceContractId = "3";

        String validationMessage = contractMappingService.performValidations(null, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractId);

        assertNotNull(validationMessage);
        assertEquals("No owner with owner id 1 found.", validationMessage);
    }

    @Test
    public void testPerformValidationsNullContractWorkerReturnsErrorMessage() {
        ContractWorker owner = new ContractWorker();
        owner.setId(1L);
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setId(3L);
        String ownerId = "1";
        String employeeNumber = "2";
        String serviceContractId = "3";

        String validationMessage = contractMappingService.performValidations(owner, null, serviceContract, ownerId, employeeNumber, serviceContractId);

        assertNotNull(validationMessage);
        assertEquals("No worker with employeeNumber 2 found.", validationMessage);
    }

    @Test
    public void testPerformValidationsOwnerIdSameAsEmployeeNumberReturnsErrorMessage() {
        ContractWorker owner = new ContractWorker();
        owner.setId(1L);
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setId(2L);
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setId(3L);
        String ownerId = "2";
        String employeeNumber = "2";
        String serviceContractId = "3";

        String validationMessage = contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractId);

        assertNotNull(validationMessage);
        assertEquals("OwnerId is same as employee Number", validationMessage);
    }

    @Test
    public void testPerformValidationsNullServiceContractReturnsErrorMessage() {
        ContractWorker owner = new ContractWorker();
        owner.setId(1L);
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setId(2L);
        String ownerId = "1";
        String employeeNumber = "2";
        String serviceContractId = "3";

        String validationMessage = contractMappingService.performValidations(owner, contractWorker, null, ownerId, employeeNumber, serviceContractId);

        assertNotNull(validationMessage);
        assertEquals("Service Contract with contract Number 3 Doesn't exist", validationMessage);
    }

    @Test
    public void testPerformValidationsNonOwnerTriesToOnboardReturnsErrorMessage() {
        ContractWorker owner = new ContractWorker();
        owner.setId(1L);
        owner.setFirstName("owner");
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setId(2L);
        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setId(3L);
        String ownerId = "1";
        String employeeNumber = "2";
        String serviceContractId = "3";
        serviceContract.setOwner(new ContractWorker());
        serviceContract.getOwner().setId(4L);
        serviceContract.getOwner().setFirstName("nonOwner");

        String validationMessage = contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractId);

        assertNotNull(validationMessage);
        assertEquals("Only the owner can onboard a contract worker.", validationMessage);
    }

    @Test
    public void testPerformValidationsNonActiveServiceContractReturnsErrorMessage() {
        ContractWorker owner = new ContractWorker();
        owner.setId(1L);
        owner.setEmployeeNumber("own1");
        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setId(2L);
        ServiceContract serviceContract = ServiceContract.builder()
                .id(3l)
                .serviceContractStatus(ServiceContractStatus.INACTIVE.getStatus())
                .contractNumber("c1")
                .owner(owner)
                .ownerId("own1").build();
        String ownerId = "1";
        String employeeNumber = "2";
        String serviceContractId = "3";

        String validationMessage = contractMappingService.performValidations(owner, contractWorker, serviceContract, ownerId, employeeNumber, serviceContractId);

        assertNotNull(validationMessage);
        assertEquals("Service Contract status is not active, Current Status: INACTIVE", validationMessage);
    }
}
