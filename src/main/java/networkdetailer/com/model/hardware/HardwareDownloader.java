package networkdetailer.com.model.hardware;

import networkdetailer.com.model.data.*;
import oshi.hardware.*;

import java.util.Arrays;

public class HardwareDownloader {
    HardwareGetter hardwareGetter;
    CPUGenerationGetter cpuGenerationGetter;
    private String cpuModel;
    private String[] cpuModelToRefactor;
    private int ramGB;
    private int diskSpaceGB;
    private String baseboardManufacturer;
    private String boardModel;
    private String bios;

    public HardwareDownloader(HardwareGetter hardwareGetter, CPUGenerationGetter cpuGenerationGetter) {
        this.hardwareGetter = hardwareGetter;
        this.cpuGenerationGetter = cpuGenerationGetter;
    }

    public CPUData getCpuData() {
        HardwareAbstractionLayer hal = hardwareGetter.getHal();
        CentralProcessor processor = hal.getProcessor();
        cpuModel = processor.getProcessorIdentifier().getName();

        cpuModelToRefactor = cpuModel.split("[ GHz]", 256);
        System.out.println(Arrays.toString(cpuModelToRefactor));

        if (cpuModelToRefactor[0].toUpperCase().contains(CPUManufacturer.INTEL.toString())) {
            return new CPUData(cpuGenerationGetter.identify(cpuModel), cpuModelToRefactor[2], Double.valueOf(cpuModelToRefactor[5]));
        } else if (cpuModelToRefactor[0].toUpperCase().contains(CPUManufacturer.AMD.toString())) {
            return new CPUData(cpuGenerationGetter.identify(cpuModel), cpuModelToRefactor[2], Double.valueOf(cpuModelToRefactor[5]));
        } else if (cpuModelToRefactor[0].toUpperCase().contains(CPUManufacturer.APPLE.toString())) {
            return new CPUData(cpuGenerationGetter.identify(cpuModel), cpuModelToRefactor[1], 0.0);
        }
        return new CPUData(cpuGenerationGetter.identify(cpuModel), cpuModelToRefactor[2], 0.0);
    }

    public MemoryData getMemoryData() {
        HardwareAbstractionLayer hal = hardwareGetter.getHal();
        GlobalMemory memory = hal.getMemory();
        ramGB = conversionBtoGB(memory.getTotal());


        // Getting disk information
        DiskType diskTypeEnum = DiskType.UNKNOWN;
        long totalDiskSpace = 0;

        for (int i = 0; i < 10; i++) {
            System.out.println(hal.getDiskStores());
        }

        for (HWDiskStore disk : hal.getDiskStores()) {
            totalDiskSpace += disk.getSize();

            // Checking the disk model and setting the appropriate type
            if (disk.getModel().toLowerCase().contains("ssd")) {
                diskTypeEnum = DiskType.SSD;  // Priority for SSD
            } else if (diskTypeEnum == DiskType.UNKNOWN && disk.getModel().toLowerCase().contains("hdd")) {
                diskTypeEnum = DiskType.HDD;  // Assign HDD only if SSD not detected
            }
        }
        diskSpaceGB = conversionBtoGB(totalDiskSpace);

        return new MemoryData(ramGB, diskSpaceGB, diskTypeEnum);
    }

    public MOBOData getMoboData() {
       ComputerSystem computerSystem = hardwareGetter.getComputerSystem();
       Baseboard baseboard = hardwareGetter.getBaseboard();
        boardModel = baseboard.getModel();
        baseboardManufacturer = baseboard.getManufacturer();
        bios = computerSystem.getFirmware().getName();

        return new MOBOData(boardModel, baseboardManufacturer, bios);
    }

    private int conversionBtoGB(long total) { // B to GB conversion
        return (int) (total / (1024 * 1024 * 1000));
    }
}