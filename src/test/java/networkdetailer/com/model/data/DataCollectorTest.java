package networkdetailer.com.model.data;

import networkdetailer.com.model.hardware.HardwareDownloader;
import networkdetailer.com.model.hardware.RequirementsChecker;
import networkdetailer.com.model.network.NetworkDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataCollectorTest {
    private DataCollector dataCollector;

    private NetworkDownloader networkDownloader;
    private HardwareDownloader hardwareDownloader;
    private RequirementsChecker requirementsChecker;

    private CPUData mockCpuData;
    private MOBOData mockMoboData;
    private MemoryData mockMemoryData;
    private NetworkData mockNetworkData;

    @BeforeEach
    public void setUp() {
        networkDownloader = mock(NetworkDownloader.class);
        hardwareDownloader = mock(HardwareDownloader.class);
        requirementsChecker = mock(RequirementsChecker.class);

        dataCollector = new DataCollector();
        dataCollector.setNetworkDownloader(networkDownloader);
        dataCollector.setHardwareDownloader(hardwareDownloader);
        dataCollector.setRequirementsChecker(requirementsChecker);

        mockCpuData = mock(CPUData.class);
        mockMemoryData = mock(MemoryData.class);
        mockMoboData = mock(MOBOData.class);
        mockNetworkData = mock(NetworkData.class);
    }

    @Test
    void getActualData_shouldReturnComputerData() {
        when(networkDownloader.getData()).thenReturn(mockNetworkData);
        when(hardwareDownloader.getCpuData()).thenReturn(mockCpuData);
        when(hardwareDownloader.getMemoryData()).thenReturn(mockMemoryData);
        when(hardwareDownloader.getMoboData()).thenReturn(mockMoboData);
        when(requirementsChecker.check(mockCpuData, mockMemoryData)).thenReturn(true);

        ComputerData result = dataCollector.getActualData();

        assertNotNull(result);
        assertEquals(mockCpuData, result.cpuData());
        assertEquals(mockMemoryData, result.memoryData());
        assertEquals(mockMoboData, result.moboData());
        assertEquals(mockNetworkData, result.networkData());
        assertEquals("true", result.getWindowsRequirements());
    }

    @Test
    void isWindowsRequirements_shouldReturnFalseWhenInvalid() {
        when(requirementsChecker.check(mockCpuData, mockMemoryData)).thenReturn(false);
        assertFalse(dataCollector.isWindowsRequirements());
    }

}

