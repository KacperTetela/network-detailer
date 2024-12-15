package networkdetailer.com.model.network;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@Slf4j
public class IPGetter {
    InetAddress get() throws SocketException {
        InetAddress ip = null;
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
                    ip = inetAddress;
                    break;
                }
            }

            if (ip != null) {
                break;
            }
        }

        if (ip == null) {
            log.warn("Could not find a suitable network interface");
        }
        return ip;
    }
}
