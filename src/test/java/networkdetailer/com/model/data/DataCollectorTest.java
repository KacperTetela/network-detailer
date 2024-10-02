package networkdetailer.com.model.data;

import networkdetailer.com.model.hardware.CPUGenerationGetter;
import networkdetailer.com.model.hardware.HardwareDownloader;
import networkdetailer.com.model.hardware.HardwareGetter;
import networkdetailer.com.model.hardware.RequirementsChecker;
import networkdetailer.com.model.network.IPGetter;
import networkdetailer.com.model.network.MacGetter;
import networkdetailer.com.model.network.NetworkDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import oshi.hardware.Baseboard;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Firmware;
import oshi.hardware.HardwareAbstractionLayer;

import static org.junit.jupiter.api.Assertions.*;

class DataCollectorTest {
    private NetworkDownloader networkDownloader;
    private HardwareDownloader hardwareDownloader;
    private RequirementsChecker requirementsChecker;

    @BeforeEach
    public void setUp() {
        // Mocking dependencies
        IPGetter ipGetter = Mockito.mock(IPGetter.class);
        MacGetter macGetter = Mockito.mock(MacGetter.class);
        networkDownloader = new NetworkDownloader(ipGetter, macGetter);

        HardwareGetter hardwareGetter = Mockito.mock(HardwareGetter.class);
        CPUGenerationGetter cpuGenerationGetter = Mockito.mock(CPUGenerationGetter.class);
        hardwareDownloader = new HardwareDownloader(hardwareGetter, cpuGenerationGetter);

        requirementsChecker = Mockito.mock(RequirementsChecker.class);

        // Setting up mocks


    }

    @Test
    public void isWindowsRequirementsPass() {
        //Given
        CPUData expectedCPU = new CPUData(new CPUGeneration(CPUManufacturer.INTEL, 9), "i7-9700K", 3.6);
        MemoryData expectedMemory = new MemoryData(16, 966, DiskType.SSD);

        //When
        boolean requirementsAnswer = requirementsChecker.check(expectedCPU, expectedMemory);

        //Then
        assertTrue(requirementsAnswer);
    }

}