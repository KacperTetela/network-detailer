package networkdetailer.com.model.data;

public record ComputerData(CPUData cpuData, MemoryData memoryData, MOBOData moboData, NetworkData networkData, String windowsRequirements) {

    public String getIP() {
        return networkData.ip();
    }

    public String getHostname() {
        return networkData.hostname();
    }

    public String getMAC() {
        return networkData.mac();
    }

    public String getCpuName() {
        return cpuData.name();
    }

    public String getCpuGen() {
        return String.valueOf(cpuData.generation().generation());
    }

    public String getCpuGhz() {
        if (Double.valueOf(cpuData.ghz()) == 0.0)
            return "UNKNOWN";
        return String.valueOf(cpuData.ghz());
    }

    public String getRamValue() {
        return String.valueOf(memoryData.ramGB());
    }

    public String getDiskSpace() {
        return String.valueOf(memoryData.diskSpaceGB());
    }

    public String getDiskType() {
        return String.valueOf(memoryData.diskType());
    }

    public String getBios() {
        return String.valueOf(moboData.bios());
    }

    public String getCpuManufacturer() {
        return String.valueOf(cpuData.generation().cpuManufacturer());
    }

    public String getWindowsRequirements() {
        return windowsRequirements;
    }
}
