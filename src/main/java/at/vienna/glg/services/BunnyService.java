package at.vienna.glg.services;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BunnyService {
	private static final Logger logger = LoggerFactory.getLogger(BunnyService.class);

	public InputStream getBootImage() {
		return this.getClass().getResourceAsStream("/boot.bin");
	}

	public String getLocalIp4Address() throws Exception {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			Enumeration<InetAddress> addresses = interfaces.nextElement().getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress address = addresses.nextElement();
				if (!address.isLoopbackAddress() && address.isSiteLocalAddress()) {
					return address.getHostAddress();
				}
			}
		}

		return null;
	}

	public void handleRFIDTag(String rfid) throws Exception {
		// XXXX: Not implemented ...
	}

}