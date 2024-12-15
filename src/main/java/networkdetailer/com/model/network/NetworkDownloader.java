package networkdetailer.com.model.network;

import lombok.extern.slf4j.Slf4j;
import networkdetailer.com.model.data.NetworkData;
import networkdetailer.com.model.util.UndefinedHostReadWrite;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

@Slf4j
public class NetworkDownloader {
    private IPGetter ipGetter;
    private MacGetter macGetter;

    public NetworkDownloader(IPGetter ipGetter, MacGetter macGetter) {
        this.ipGetter = ipGetter;
        this.macGetter = macGetter;
    }

    public NetworkData getData() {
        InetAddress ip;
        String ipHostAddress = "Unknown IP";
        String macAddress = "Unknown MAC";
        String hostName = "Undefined";

        try {
            ip = ipGetter.get();

            ipHostAddress = ip.getHostAddress();
            macAddress = macGetter.get(ip);
            hostName = ip.getHostName();
            log.trace(hostName);

            // Remove the suffix (e.g. ".home") from the hostname
            if (hostName.contains(".")) {
                hostName = hostName.split("\\.")[0];
            }

            log.trace("IP Address: " + ipHostAddress);
            log.trace("MAC Address: " + macAddress);
            log.trace("Hostname: " + hostName);


        } catch (SocketException e) {
            log.error("Error getting MAC address: " + e.getMessage());
        } catch (UnknownHostException e) {
            log.error("Error getting hostname: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error getting network object: " + e.getMessage());
        }

        if (hostName.equals("Undefined")) {
            hostName = hostName + UndefinedHostReadWrite.readNumberFromFile();
            UndefinedHostReadWrite.incrementNumber();
        }

        return new NetworkData(ipHostAddress, hostName, macAddress);
    }

}