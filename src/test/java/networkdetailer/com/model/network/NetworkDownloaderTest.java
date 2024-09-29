package networkdetailer.com.model.network;

import networkdetailer.com.model.data.NetworkData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class NetworkDownloaderTest {

    @Test
    public void dataConnectionShouldGenerateRightNetworkData() throws SocketException, UnknownHostException {
        //Given
        String hostname = "DESKTOP-SHNJU06";
        IPGetter ipGetter = Mockito.mock(IPGetter.class);
        MacGetter macGetter = Mockito.mock(MacGetter.class);
        InetAddress customInetAddress = Mockito.mock(InetAddress.class);
        Mockito.when(customInetAddress.getHostAddress()).thenReturn("192.168.1.22");
        Mockito.when(customInetAddress.getHostName()).thenReturn(hostname + ".home");

        Mockito.when(ipGetter.get()).thenReturn(customInetAddress);
        Mockito.when(macGetter.get(customInetAddress)).thenReturn("00:D8:61:32:CF:86");
        NetworkDownloader networkDownloader = new NetworkDownloader(ipGetter, macGetter);

        //When
        NetworkData networkData = networkDownloader.getData();

        //Then
        assertEquals(networkData.hostname(), hostname);
        assertEquals(networkData.ip(), customInetAddress.getHostAddress());
        assertEquals(networkData.mac(), macGetter.get(customInetAddress));
    }

}