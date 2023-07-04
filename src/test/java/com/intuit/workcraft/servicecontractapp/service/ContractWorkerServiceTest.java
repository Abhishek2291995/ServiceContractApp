package com.intuit.workcraft.servicecontractapp.service;

import com.intuit.workcraft.servicecontractapp.model.ContractWorker;
import com.intuit.workcraft.servicecontractapp.repository.ContractWorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;

public class ContractWorkerServiceTest {

    @Mock
    private ContractWorkerRepository contractWorkerRepository;

    @InjectMocks
    private ContractWorkerService contractWorkerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveContractWorker_ValidContractWorker_ReturnsSavedContractWorker() {

        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber("123");
        Mockito.when(contractWorkerRepository.findContractWorkerByEmployeeNumber("123")).thenReturn(null);
        Mockito.when(contractWorkerRepository.save(contractWorker)).thenReturn(contractWorker);

        ContractWorker savedContractWorker = contractWorkerService.saveContractWorker(contractWorker);

        // Assert
        assertNotNull(savedContractWorker);
        assertEquals(contractWorker, savedContractWorker);
        Mockito.verify(contractWorkerRepository, Mockito.times(1)).findContractWorkerByEmployeeNumber("123");
        Mockito.verify(contractWorkerRepository, Mockito.times(1)).save(contractWorker);
    }

    @Test
    public void testSaveContractWorker_ExistingContractWorker_ReturnsNull() {

        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber("123");
        Mockito.when(contractWorkerRepository.findContractWorkerByEmployeeNumber("123")).thenReturn(contractWorker);

        ContractWorker savedContractWorker = contractWorkerService.saveContractWorker(contractWorker);

        // Assert
        assertNull(savedContractWorker);
        Mockito.verify(contractWorkerRepository, Mockito.times(1)).findContractWorkerByEmployeeNumber("123");
        Mockito.verify(contractWorkerRepository, Mockito.never()).save(Mockito.any(ContractWorker.class));
    }

    @Test
    public void testGetContractWorker_ExistingEmployeeNumber_ReturnsContractWorker() {

        ContractWorker contractWorker = new ContractWorker();
        contractWorker.setEmployeeNumber("123");
        Mockito.when(contractWorkerRepository.findContractWorkerByEmployeeNumber("123")).thenReturn(contractWorker);

        ContractWorker retrievedContractWorker = contractWorkerService.getContractWorker("123");

        // Assert
        assertNotNull(retrievedContractWorker);
        assertEquals(contractWorker, retrievedContractWorker);
        Mockito.verify(contractWorkerRepository, Mockito.times(1)).findContractWorkerByEmployeeNumber("123");
    }

    @Test
    public void testGetContractWorker_NonExistingEmployeeNumber_ReturnsNull() {

        Mockito.when(contractWorkerRepository.findContractWorkerByEmployeeNumber("123")).thenReturn(null);

        ContractWorker retrievedContractWorker = contractWorkerService.getContractWorker("123");

        // Assert
        assertNull(retrievedContractWorker);
        Mockito.verify(contractWorkerRepository, Mockito.times(1)).findContractWorkerByEmployeeNumber("123");
    }

    @Test
    public void testGetAllWorkers_ReturnsListOfContractWorkers() {

        List<ContractWorker> contractWorkers = new ArrayList<>();
        contractWorkers.add(new ContractWorker());
        contractWorkers.add(new ContractWorker());
        Mockito.when(contractWorkerRepository.findAll()).thenReturn(contractWorkers);

        List<ContractWorker> allWorkers = contractWorkerService.getAllWorkers();

        // Assert
        assertNotNull(allWorkers);
        assertEquals(2, allWorkers.size());
        Mockito.verify(contractWorkerRepository, Mockito.times(1)).findAll();
    }
}
