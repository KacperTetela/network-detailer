package networkdetailer.com.model.network;

import lombok.extern.slf4j.Slf4j;

import java.net.NetworkInterface;
import java.util.*;

/**
 * Ability to obtain a mac address without being connected to a network.
 */

@Slf4j
public class MacGetterOffline {

    public String get() {
        Set<String> uniqueMacs = new HashSet<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface networkInterface : Collections.list(interfaces)) {
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null && !isVirtualInterface(networkInterface.getDisplayName())) {
                    StringBuilder macAddress = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        macAddress.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    if (uniqueMacs.add(macAddress.toString())) {
                        log.trace("Interface: {} -> MAC Address: {}", networkInterface.getDisplayName(), macAddress);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(uniqueMacs).get(0); //todo
    }

    private boolean isVirtualInterface(String displayName) {
        String[] virtualKeywords = {"Virtual", "Hyper-V", "VMware", "Loopback", "Host-Only", "VPN", "Fortinet"};
        for (String keyword : virtualKeywords) {
            if (displayName != null && displayName.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
