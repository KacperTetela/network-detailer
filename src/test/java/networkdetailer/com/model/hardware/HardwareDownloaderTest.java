package networkdetailer.com.model.hardware;

import networkdetailer.com.model.data.CPUData;
import networkdetailer.com.model.data.DiskType;
import networkdetailer.com.model.data.MOBOData;
import networkdetailer.com.model.data.MemoryData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import oshi.hardware.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HardwareDownloaderTest {
    private HardwareDownloader hardwareDownloader;
    private HardwareAbstractionLayer hal;

    private String bios = "1.C0";
    private String cpuModel = "i7-9700K";
    private String cpuManufacturer = "Intel";

    @BeforeEach
    public void setUp() {

        //Given
        HardwareGetter hardwareGetter = Mockito.mock(HardwareGetter.class);
        CPUGenerationGetter cpuGenerationGetter = Mockito.mock(CPUGenerationGetter.class);
        hal = Mockito.mock(HardwareAbstractionLayer.class);
        Mockito.when(hardwareGetter.getHal()).thenReturn(hal);

        ComputerSystem computerSystem = Mockito.mock(ComputerSystem.class);
        Mockito.when(hardwareGetter.getComputerSystem()).thenReturn(computerSystem);

        Firmware firmware = Mockito.mock(Firmware.class);
        Mockito.when(firmware.getName()).thenReturn(bios);
        Mockito.when(computerSystem.getFirmware()).thenReturn(firmware);

        Baseboard baseboard = Mockito.mock(Baseboard.class);
        Mockito.when(baseboard.getManufacturer()).thenReturn(cpuManufacturer);
        Mockito.when(baseboard.getModel()).thenReturn(cpuModel);
        Mockito.when(hardwareGetter.getBaseboard()).thenReturn(baseboard);

        hardwareDownloader = new HardwareDownloader(hardwareGetter, cpuGenerationGetter);

    }

    @Test
    public void getMOBODataTest() {

        //When
        MOBOData moboData = hardwareDownloader.getMoboData();

        //Then
        assertEquals(moboData.bios(), bios);
        assertEquals(moboData.manufacturer(), cpuManufacturer);
        assertEquals(moboData.model(), cpuModel);
    }

    @Test
    public void getMemoryDataTest() {
        //Given
        long ramB = 34293080064L;
        long disk1Space = 2000396321280L;

        HardwareGetter hardwareGetter = Mockito.mock(HardwareGetter.class);
        Mockito.when(hardwareGetter.getHal()).thenReturn(hal);

        GlobalMemory globalMemory = Mockito.mock(GlobalMemory.class);
        Mockito.when(hal.getMemory()).thenReturn(globalMemory);
        Mockito.when(globalMemory.getTotal()).thenReturn(ramB);

        HWDiskStore hwDiskStore = Mockito.mock(HWDiskStore.class);
        Mockito.when(hwDiskStore.getSize()).thenReturn(disk1Space);
        Mockito.when(hal.getDiskStores()).thenReturn(List.of(hwDiskStore));

        Mockito.when(hwDiskStore.getModel()).thenReturn("Samsung SSD 860");


        //When
        MemoryData memoryData = hardwareDownloader.getMemoryData();

        //Then
        assertEquals(memoryData.ramGB(), 32);
        assertEquals(memoryData.diskSpaceGB(), 1907);
        assertEquals(memoryData.diskType(), DiskType.SSD);
    }

    @Test
    public void getCPUDataTest() {
        //Given
        CentralProcessor processor = Mockito.mock(CentralProcessor.class);
        CentralProcessor.ProcessorIdentifier processorIdentifier = Mockito.mock(CentralProcessor.ProcessorIdentifier.class);

        // Mockowanie zwracania ProcessorIdentifier z CentralProcessor
        Mockito.when(processor.getProcessorIdentifier()).thenReturn(processorIdentifier);

        // Mockowanie zwracania nazwy procesora
        Mockito.when(processorIdentifier.getName()).thenReturn("[Intel(R), Core(TM), i7-9700K, CPU, @, 3.60, , , ]");

        // Mockowanie metody getProcessor w HardwareAbstractionLayer (hal)
        Mockito.when(hal.getProcessor()).thenReturn(processor);


        //When
        CPUData cpuData = hardwareDownloader.getCpuData();

        //Then
        assertEquals(cpuData.name(), "i7-9700K");
    }

}