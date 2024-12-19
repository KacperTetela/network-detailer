package networkdetailer.com.model.network;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@Slf4j
public class IPGetter {
    //returns null if offline
    InetAddress get() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                // Ignoring virtual interfaces
                if (networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();

                    // Checking if the address is IPv4 and not a link-local address (127.0.0.1)
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().indexOf(':') == -1) {
                        return inetAddress;
                    }
                }
            }
            log.warn("Could not find a suitable network interface - computer offline");

        } catch (SocketException e) {
            log.error("computer offline: Error getting network interfaces: " + e.getMessage());
        }

        return null;
    }
}
