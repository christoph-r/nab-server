package at.vienna.glg.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import at.vienna.glg.model.Ping;
import fi.uta.cs.nabaztag.Choreography;
import fi.uta.cs.nabaztag.Packet;

@Service
public class BunnyService {
	private static final Logger logger = LoggerFactory.getLogger(BunnyService.class);

	@Value("${nab.records.path}")
	private String rootPath;

	@Autowired
	private MessageQueue queue;

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

	public Packet getNextCommand() {
		Packet p = new Packet();

		if (queue.hasNext()) {
			p = queue.next();
			p.addBlock(new Ping(10));
		} else {
			p.addBlock(new Ping());
		}

		return p;
	}

	public void handleRFIDTag(String rfid) throws Exception {
		// XXXX: Not implemented ...
	}

	public void saveRecord(byte[] record) throws Exception {
		String file = rootPath + "record.wav";
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(file)));
		stream.write(record);
		stream.close();
	}

	public FileInputStream loadRecord(String recordId) throws Exception {
		return new FileInputStream(new File(rootPath + recordId));
	}

	public int[] getChoreography(String choreoId) throws Exception {
		Choreography cb = ChoreoFactory.getChoreography(choreoId);

		if (cb != null) {
			return cb.getContent();
		}

		logger.warn("no choreo found for {}", choreoId);
		return null;
	}
}