package com.intuit.workcraft.servicecontractapp.service;

import com.intuit.workcraft.servicecontractapp.model.ServiceContract;
import com.intuit.workcraft.servicecontractapp.repository.ServiceContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class ServiceContractServiceTest {

    @Mock
    private ServiceContractRepository serviceContractRepository;

    @InjectMocks
    private ServiceContractService serviceContractService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveServiceContractValidServiceContractReturnsSavedServiceContract() {

        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber("123");
        Mockito.when(serviceContractRepository.getServiceContractByContractNumber("123")).thenReturn(null);
        Mockito.when(serviceContractRepository.save(serviceContract)).thenReturn(serviceContract);

        ServiceContract savedServiceContract = serviceContractService.saveServiceContract(serviceContract);

        // Assert
        assertNotNull(savedServiceContract);
        assertEquals(serviceContract, savedServiceContract);
        Mockito.verify(serviceContractRepository, Mockito.times(1)).getServiceContractByContractNumber("123");
        Mockito.verify(serviceContractRepository, Mockito.times(1)).save(serviceContract);
    }

    @Test
    public void testSaveServiceContractExistingServiceContractReturnsNull() {

        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber("123");
        Mockito.when(serviceContractRepository.getServiceContractByContractNumber("123")).thenReturn(serviceContract);

        ServiceContract savedServiceContract = serviceContractService.saveServiceContract(serviceContract);

        // Assert
        assertNull(savedServiceContract);
        Mockito.verify(serviceContractRepository, Mockito.times(1)).getServiceContractByContractNumber("123");
        Mockito.verify(serviceContractRepository, Mockito.never()).save(Mockito.any(ServiceContract.class));
    }

    @Test
    public void testGetServiceContractExistingContractNumberReturnsServiceContract() {

        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber("123");
        Mockito.when(serviceContractRepository.getServiceContractByContractNumber("123")).thenReturn(serviceContract);

        ServiceContract retrievedServiceContract = serviceContractService.getServiceContract("123");

        // Assert
        assertNotNull(retrievedServiceContract);
        assertEquals(serviceContract, retrievedServiceContract);
        Mockito.verify(serviceContractRepository, Mockito.times(1)).getServiceContractByContractNumber("123");
    }

    @Test
    public void testGetServiceContractNonExistingContractNumberReturnsNull() {

        Mockito.when(serviceContractRepository.getServiceContractByContractNumber("123")).thenReturn(null);

        ServiceContract retrievedServiceContract = serviceContractService.getServiceContract("123");

        // Assert
        assertNull(retrievedServiceContract);
        Mockito.verify(serviceContractRepository, Mockito.times(1)).getServiceContractByContractNumber("123");
    }

    @Test
    public void testGetAllServiceContractsReturnsListOfServiceContracts() {

        List<ServiceContract> serviceContracts = new ArrayList<>();
        serviceContracts.add(new ServiceContract());
        serviceContracts.add(new ServiceContract());
        Mockito.when(serviceContractRepository.findAll()).thenReturn(serviceContracts);

        List<ServiceContract> allServiceContracts = serviceContractService.getAllServiceContracts();

        // Assert
        assertNotNull(allServiceContracts);
        assertEquals(2, allServiceContracts.size());
        Mockito.verify(serviceContractRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testUpdateServiceContractReturnsUpdatedServiceContract() {

        ServiceContract serviceContract = new ServiceContract();
        serviceContract.setContractNumber("123");
        Mockito.when(serviceContractRepository.saveAndFlush(serviceContract)).thenReturn(serviceContract);

        ServiceContract updatedServiceContract = serviceContractService.updateServiceContract(serviceContract);

        // Assert
        assertNotNull(updatedServiceContract);
        assertEquals(serviceContract, updatedServiceContract);
        Mockito.verify(serviceContractRepository, Mockito.times(1)).saveAndFlush(serviceContract);
    }
}
