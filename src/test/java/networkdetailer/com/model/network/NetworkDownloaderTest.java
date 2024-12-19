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
        MacGetterOnline macGetterOnline = Mockito.mock(MacGetterOnline.class);
        MacGetterOffline macGetterOffline = Mockito.mock(MacGetterOffline.class);
        InetAddress customInetAddress = Mockito.mock(InetAddress.class);
        Mockito.when(customInetAddress.getHostAddress()).thenReturn("192.168.1.22");
        Mockito.when(customInetAddress.getHostName()).thenReturn(hostname + ".home");

        Mockito.when(ipGetter.get()).thenReturn(customInetAddress);
        Mockito.when(macGetterOnline.get(customInetAddress)).thenReturn("00:D8:61:32:CF:86");
        NetworkDownloader networkDownloader = new NetworkDownloader(ipGetter, macGetterOnline, macGetterOffline);

        //When
        NetworkData networkData = networkDownloader.getData();

        //Then
        assertEquals(networkData.hostname(), hostname);
        assertEquals(networkData.ip(), customInetAddress.getHostAddress());
        assertEquals(networkData.mac(), macGetterOnline.get(customInetAddress));
    }

    @Test
    public void shouldHandleSocketExceptionAndReturnDefaultValues() throws SocketException {
        // Given
        IPGetter ipGetter = null;
        MacGetterOnline macGetterOnline = Mockito.mock(MacGetterOnline.class);
        MacGetterOffline macGetterOffline = Mockito.mock(MacGetterOffline.class);
        NetworkDownloader networkDownloader = new NetworkDownloader(ipGetter, macGetterOnline, macGetterOffline);

        // Then
        assertEquals("Unknown IP", networkDownloader.getData().ip());
    }

    @Test
    public void shouldHandleHostnameWithoutSuffix() throws SocketException, UnknownHostException {
        // Given
        String hostname = "DESKTOP-SHNJU06";
        IPGetter ipGetter = Mockito.mock(IPGetter.class);
        MacGetterOnline macGetterOnline = Mockito.mock(MacGetterOnline.class);
        MacGetterOffline macGetterOffline = Mockito.mock(MacGetterOffline.class);
        InetAddress customInetAddress = Mockito.mock(InetAddress.class);
        Mockito.when(customInetAddress.getHostAddress()).thenReturn("192.168.1.22");
        Mockito.when(customInetAddress.getHostName()).thenReturn(hostname);

        Mockito.when(ipGetter.get()).thenReturn(customInetAddress);
        Mockito.when(macGetterOnline.get(customInetAddress)).thenReturn("00:D8:61:32:CF:86");
        NetworkDownloader networkDownloader = new NetworkDownloader(ipGetter, macGetterOnline, macGetterOffline);

        // When
        NetworkData networkData = networkDownloader.getData();

        // Then
        assertEquals(networkData.hostname(), hostname);
        assertEquals(networkData.ip(), customInetAddress.getHostAddress());
        assertEquals(networkData.mac(), macGetterOnline.get(customInetAddress));
    }
}