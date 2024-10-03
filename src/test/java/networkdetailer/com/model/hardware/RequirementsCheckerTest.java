package networkdetailer.com.model.hardware;

import networkdetailer.com.model.data.CPUData;
import networkdetailer.com.model.data.CPUGeneration;
import networkdetailer.com.model.data.CPUManufacturer;
import networkdetailer.com.model.data.MemoryData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequirementsCheckerTest {
    private RequirementsChecker requirementsChecker;

    @BeforeEach
    void setUp() {
        requirementsChecker = new RequirementsChecker();
    }

    @Test
    void check_shouldReturnTrueForValidIntelCPU() {
        CPUData cpuData = mock(CPUData.class);
        CPUGeneration cpuGeneration = mock(CPUGeneration.class);
        MemoryData memoryData = mock(MemoryData.class);

        when(cpuData.generation()).thenReturn(cpuGeneration);
        when(cpuGeneration.cpuManufacturer()).thenReturn(CPUManufacturer.INTEL);
        when(cpuGeneration.generation()).thenReturn(8);

        when(memoryData.ramGB()).thenReturn(8);
        when(memoryData.diskSpaceGB()).thenReturn(128);

        boolean result = requirementsChecker.check(cpuData, memoryData);
        assertTrue(result);
    }

    @Test
    void check_shouldReturnFalseForOldIntelCPU() {
        CPUData cpuData = mock(CPUData.class);
        CPUGeneration cpuGeneration = mock(CPUGeneration.class);
        MemoryData memoryData = mock(MemoryData.class);

        when(cpuData.generation()).thenReturn(cpuGeneration);
        when(cpuGeneration.cpuManufacturer()).thenReturn(CPUManufacturer.INTEL);
        when(cpuGeneration.generation()).thenReturn(7);

        when(memoryData.ramGB()).thenReturn(8);
        when(memoryData.diskSpaceGB()).thenReturn(128);

        boolean result = requirementsChecker.check(cpuData, memoryData);
        assertFalse(result);
    }

    @Test
    void check_shouldReturnFalseForLowRAM() {
        CPUData cpuData = mock(CPUData.class);
        CPUGeneration cpuGeneration = mock(CPUGeneration.class);
        MemoryData memoryData = mock(MemoryData.class);

        when(cpuData.generation()).thenReturn(cpuGeneration);
        when(cpuGeneration.cpuManufacturer()).thenReturn(CPUManufacturer.INTEL);
        when(cpuGeneration.generation()).thenReturn(8);

        when(memoryData.ramGB()).thenReturn(2);
        when(memoryData.diskSpaceGB()).thenReturn(128);

        boolean result = requirementsChecker.check(cpuData, memoryData);
        assertFalse(result);
    }

    @Test
    void check_shouldReturnFalseForLowDiskSpace() {
        CPUData cpuData = mock(CPUData.class);
        CPUGeneration cpuGeneration = mock(CPUGeneration.class);
        MemoryData memoryData = mock(MemoryData.class);

        when(cpuData.generation()).thenReturn(cpuGeneration);
        when(cpuGeneration.cpuManufacturer()).thenReturn(CPUManufacturer.AMD);
        when(cpuGeneration.generation()).thenReturn(3);

        when(memoryData.ramGB()).thenReturn(8);
        when(memoryData.diskSpaceGB()).thenReturn(32);

        boolean result = requirementsChecker.check(cpuData, memoryData);
        assertFalse(result);
    }
}