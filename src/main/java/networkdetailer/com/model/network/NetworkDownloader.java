package networkdetailer.com.model.network;

import networkdetailer.com.model.data.NetworkData;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class NetworkDownloader {
    private IPGetter ipGetter;
    private MacGetter macGetter;

    public NetworkDownloader(IPGetter ipGetter, MacGetter macGetter) {
        this.ipGetter = ipGetter;
        this.macGetter = macGetter;
    }

    public NetworkData getData() {
        InetAddress ip;
        String ipHostAddress;
        String macAddress;
        String hostName;

        try {
            ip = ipGetter.get();
            ipHostAddress = ip.getHostAddress();
            macAddress = macGetter.get(ip);
            hostName = ip.getHostName();

            System.out.println(hostName);

            // Remove the suffix (e.g. ".home") from the hostname
            if (hostName.contains(".")) {
                hostName = hostName.split("\\.")[0];
            }

            System.out.println("IP Address: " + ipHostAddress);
            System.out.println("MAC Address: " + macAddress);
            System.out.println("Hostname: " + hostName);

            return new NetworkData(ipHostAddress, hostName, macAddress);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("IP data not initialized");
    }

}