package networkdetailer.com.model.api;

public record DatabaseDTO(
        String hostname,
        String ipAddress,
        String macAddress,
        String cpuManufacture,
        String cpuName,
        int cpuGeneration,
        double cpuGHz,
        int ramGB,
        int diskspaceGB,
        String diskType,
        String biosVersion,
        String windowsRequirements
) {}
