package networkdetailer.com.model.hardware;

import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;

public class HardwareGetter {

    public synchronized SystemInfo getSystemInfo() {
        return new SystemInfo();
    }

    public synchronized ComputerSystem getComputerSystem() {
        ComputerSystem computerSystem = getSystemInfo().getHardware().getComputerSystem();
        return computerSystem;
    }

    public synchronized HardwareAbstractionLayer getHal() {
        return getSystemInfo().getHardware();
    }

    public Baseboard getBaseboard() {
        return getComputerSystem().getBaseboard();
    }
}
