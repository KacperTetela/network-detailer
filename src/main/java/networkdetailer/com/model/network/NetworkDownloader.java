package networkdetailer.com.model.network;

import lombok.extern.slf4j.Slf4j;
import networkdetailer.com.model.data.NetworkData;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class NetworkDownloader {
    private IPGetter ipGetter;
    private MacGetterOnline macGetterOnline;
    private MacGetterOffline macGetterOffline;

    public NetworkDownloader(IPGetter ipGetter, MacGetterOnline macGetterOnline, MacGetterOffline macGetterOffline) {
        this.ipGetter = ipGetter;
        this.macGetterOnline = macGetterOnline;
        this.macGetterOffline = macGetterOffline;
    }

    public NetworkData getData() {
        InetAddress ip;
        String ipHostAddress = "Unknown IP";
        String macAddress = "Unknown MAC";
        String hostName = "Undefined";

        try {
            ip = ipGetter.get();

            if (ip == null) { // Computer offline
                try {
                    hostName = InetAddress.getLocalHost().getHostName();
                    macAddress = macGetterOffline.get();
                } catch (UnknownHostException ex) {
                    hostName = "Unknown";
                }
                return new NetworkData(ipHostAddress, hostName, macAddress, false);
            }


            ipHostAddress = ip.getHostAddress();
            macAddress = macGetterOnline.get(ip);
            hostName = ip.getHostName();
            log.trace(hostName);

            // Remove the suffix (e.g. ".home") from the hostname
            if (hostName.contains(".")) {
                hostName = hostName.split("\\.")[0];
            }

            log.trace("IP Address: " + ipHostAddress);
            log.trace("MAC Address: " + macAddress);
            log.trace("Hostname: " + hostName);


        }  catch (UnknownHostException e) {
            log.error("Error getting hostname: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error getting network object: " + e.getMessage());
        }

        return new NetworkData(ipHostAddress, hostName, macAddress, true);
    }

}