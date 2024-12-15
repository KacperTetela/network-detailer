package networkdetailer.com.model.hardware;

import lombok.extern.slf4j.Slf4j;
import networkdetailer.com.model.data.CPUData;
import networkdetailer.com.model.data.CPUManufacturer;
import networkdetailer.com.model.data.MemoryData;

@Slf4j
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
        boolean requrementsMet = cpuOk && ramOk && diskOk;
        if (requrementsMet) {
            log.trace("The hardware meets the minimum system requirements for Windows 11");
        } else {
            log.warn("Your hardware does not meet the minimum system requirements for Windows 11");
            if (!cpuOk) log.warn("- CPU does not meet generation requirements (min. 8)");
            if (!ramOk) log.warn("- RAM does not meet requirements");
            if (!diskOk) log.warn("- The disk does not meet the requirements");
        }
        log.info("Disk type: " + memoryData.diskType());
        return requrementsMet;
    }
}