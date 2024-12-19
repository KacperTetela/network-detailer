package networkdetailer.com.model.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class MacGetterOnline {
    private NetworkInterface network;

    String get(InetAddress ip) throws UnknownHostException, SocketException {

        network = NetworkInterface.getByInetAddress(ip);
        if (network != null) {
            // Getting MAC address
            byte[] macAddressBytes = network.getHardwareAddress();

            if (macAddressBytes != null) {
                // Converting the MAC address to a readable form (e.g. 00-1A-2B-3C-4D-5E)
                StringBuilder macAddressBuilder = new StringBuilder();
                for (int i = 0; i < macAddressBytes.length; i++) {
                    macAddressBuilder.append(String.format("%02X", macAddressBytes[i]));
                    if (i < macAddressBytes.length - 1) {
                        macAddressBuilder.append(":");
                    }
                }
                return macAddressBuilder.toString();
            }
        }
        throw new RuntimeException("Network object not initialized");
    }

}
