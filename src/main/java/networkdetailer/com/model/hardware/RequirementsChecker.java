package networkdetailer.com.model.hardware;

import networkdetailer.com.model.data.CPUData;
import networkdetailer.com.model.data.CPUManufacturer;
import networkdetailer.com.model.data.MemoryData;

public class RequirementsChecker {
    private final int MIN_INTEL_CPU_GENERATION = 8; // Minimum requirements
    private final int MIN_AMD_CPU_GENERATION = 2;
    private final long MIN_RAM_GB = 4;
    private final long MIN_DISK_SPACE_GB = 64;

    public boolean check(CPUData cpuData, MemoryData memoryData) {
        boolean cpuOk;
        switch (cpuData.generation().cpuManufacturer()){
            case CPUManufacturer.INTEL -> cpuOk = cpuData.generation().generation() >= MIN_INTEL_CPU_GENERATION;
            case CPUManufacturer.AMD -> cpuOk = cpuData.generation().generation() >= MIN_AMD_CPU_GENERATION;
            default -> cpuOk = false;
        }

        boolean ramOk = memoryData.ramGB() >= MIN_RAM_GB;
        boolean diskOk = memoryData.diskSpaceGB() >= MIN_DISK_SPACE_GB;

        if (cpuOk && ramOk && diskOk) {
            System.out.println("The hardware meets the minimum system requirements for Windows 11");
            System.out.println("Disk type: " + memoryData.diskType());
            return true;
        } else {
            System.out.println("Your hardware does not meet the minimum system requirements for Windows 11");
            if (!cpuOk) System.out.println("- CPU does not meet generation requirements (min. 8)");
            if (!ramOk) System.out.println("- RAM does not meet requirements");
            if (!diskOk) System.out.println("- The disk does not meet the requirements");
            System.out.println("Disk type: " + memoryData.diskType());
            return false;
        }
    }
}