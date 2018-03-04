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

import at.vienna.glg.model.Choreo;
import at.vienna.glg.model.Ping;
import at.vienna.glg.model.Record;
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

	public void initTestData() {
		Packet p = new Packet();
		p.addBlock(new Ping(10));
		queue.push(p);

		p = new Packet();
		p.addBlock(new Record("record.wav", true));
		queue.push(p);

		p = new Packet();
		p.addBlock(new Ping(10));
		queue.push(p);

		p = new Packet();
		p.addBlock(new Choreo("demo"));
		queue.push(p);

		p = new Packet();
		p.addBlock(new Ping(10));
		queue.push(p);

		p = new Packet();
		p.addBlock(new Record("test.mp3", true));
		queue.push(p);
	}

	public Packet getNextCommand() {
		Packet next = queue.next();

		if (next != null) {
			return next;
		}
		Packet p = new Packet();
		p.addBlock(new Ping());
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

	public int[] loadChoreography(String choreoId) throws Exception {
		Choreography cb = new Choreography(choreoId);

		cb.addEarMove(10, Choreography.EAR_RIGHT, 0, Choreography.DIRECTION_FORWARD);
		cb.addEarMove(10, Choreography.EAR_LEFT, 12, Choreography.DIRECTION_BACKWARD);
		cb.addLedCommand(20, Choreography.LED_CENTER, 255, 0, 0);
		cb.addLedCommand(20, Choreography.LED_RIGHT, 0, 255, 0);
		cb.addLedCommand(20, Choreography.LED_LEFT, 0, 0, 255);

		return cb.getContent();
	}
}