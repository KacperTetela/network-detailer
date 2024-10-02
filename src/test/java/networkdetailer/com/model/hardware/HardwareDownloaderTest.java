package networkdetailer.com.model.hardware;

import networkdetailer.com.model.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import oshi.hardware.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HardwareDownloaderTest {
    private HardwareDownloader hardwareDownloader;
    private HardwareAbstractionLayer hal;
    private CPUGenerationGetter cpuGenerationGetter;
    private HardwareGetter hardwareGetter;

    private String bios = "1.C0";
    private String cpuModel = "i7-9700K";
    private String cpuManufacturer = "Intel";
    private CPUGeneration expectedGeneration;

    @BeforeEach
    public void setUp() {
        // Mocking dependencies
        hardwareGetter = Mockito.mock(HardwareGetter.class);
        cpuGenerationGetter = Mockito.mock(CPUGenerationGetter.class);
        hal = Mockito.mock(HardwareAbstractionLayer.class);
        expectedGeneration = new CPUGeneration(CPUManufacturer.INTEL, 9);

        // Setting up mocks
        Mockito.when(hardwareGetter.getHal()).thenReturn(hal);

        // Mocking baseboard and computer system
        ComputerSystem computerSystem = Mockito.mock(ComputerSystem.class);
        Firmware firmware = Mockito.mock(Firmware.class);
        Baseboard baseboard = Mockito.mock(Baseboard.class);

        Mockito.when(hardwareGetter.getComputerSystem()).thenReturn(computerSystem);
        Mockito.when(computerSystem.getFirmware()).thenReturn(firmware);
        Mockito.when(firmware.getName()).thenReturn(bios);

        Mockito.when(hardwareGetter.getBaseboard()).thenReturn(baseboard);
        Mockito.when(baseboard.getManufacturer()).thenReturn(cpuManufacturer);
        Mockito.when(baseboard.getModel()).thenReturn(cpuModel);

        hardwareDownloader = new HardwareDownloader(hardwareGetter, cpuGenerationGetter);
    }

    @Test
    public void getMOBODataTest() {
        // When
        MOBOData moboData = hardwareDownloader.getMoboData();

        // Then
        assertEquals(moboData.bios(), bios);
        assertEquals(moboData.manufacturer(), cpuManufacturer);
        assertEquals(moboData.model(), cpuModel);
    }

    @Test
    public void getMemoryDataTest() {
        // Given
        long ramB = 34293080064L;
        long disk1Space = 2000396321280L;
        GlobalMemory globalMemory = Mockito.mock(GlobalMemory.class);
        HWDiskStore hwDiskStore = Mockito.mock(HWDiskStore.class);

        // Mocking memory and disk store
        Mockito.when(hal.getMemory()).thenReturn(globalMemory);
        Mockito.when(globalMemory.getTotal()).thenReturn(ramB);
        Mockito.when(hwDiskStore.getSize()).thenReturn(disk1Space);
        Mockito.when(hwDiskStore.getModel()).thenReturn("Samsung SSD 860");
        Mockito.when(hal.getDiskStores()).thenReturn(List.of(hwDiskStore));

        // When
        MemoryData memoryData = hardwareDownloader.getMemoryData();

        // Then
        assertEquals(memoryData.ramGB(), 32);
        assertEquals(memoryData.diskSpaceGB(), 1907);
        assertEquals(memoryData.diskType(), DiskType.SSD);
    }

    @Test
    public void getCPUDataTest() {
        // Given
        CentralProcessor processor = Mockito.mock(CentralProcessor.class);
        CentralProcessor.ProcessorIdentifier processorIdentifier = Mockito.mock(CentralProcessor.ProcessorIdentifier.class);

        Mockito.when(processor.getProcessorIdentifier()).thenReturn(processorIdentifier);
        Mockito.when(processorIdentifier.getName()).thenReturn("Intel(R) Core(TM) i7-9700K CPU @ 3.60GHz");
        Mockito.when(hal.getProcessor()).thenReturn(processor);
        Mockito.when(cpuGenerationGetter.identify(Mockito.anyString())).thenReturn(expectedGeneration);

        // When
        CPUData cpuData = hardwareDownloader.getCpuData();

        // Then
        assertEquals(cpuData.name(), "i7-9700K");
        assertEquals(cpuData.generation().generation(), 9);
        assertEquals(cpuData.generation().cpuManufacturer(), CPUManufacturer.INTEL);
        assertEquals(cpuData.ghz(), 3.6);
    }
}