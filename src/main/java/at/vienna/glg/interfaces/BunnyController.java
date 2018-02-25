package at.vienna.glg.interfaces;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.vienna.glg.services.BunnyService;

/**
 * Handles Backend connections for Nabaztag bunny.
 * 
 * @see {@link http://www.sis.uta.fi/~spi/jnabserver/}
 */
@RestController
@RequestMapping("vl/")
public class BunnyController {
	private static final Logger logger = LoggerFactory.getLogger(BunnyController.class);

	@Autowired
	private BunnyService bunnyService;

	@GetMapping(value = "/bc.jsp", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public InputStreamResource getBootOnStartup(@RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit MAC {} with version {} started", q.get("m"), q.get("v"));
		return new InputStreamResource(bunnyService.getBootImage());
	}

	@GetMapping(value = "/locate.jsp", produces = { MediaType.TEXT_PLAIN_VALUE })
	public String getServerLocations(@RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit {} got located", q.get("sn"));
		String myIp = bunnyService.getLocalIp4Address();
		String resp = "ping " + myIp + "\n";
		resp += "broad " + myIp;
		return resp;
	}

	@GetMapping(value = "/p4.jsp", produces = { MediaType.TEXT_PLAIN_VALUE })
	public String getUpdates(@RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit {} sent status {}", q.get("sn"), q.get("sd"));
		return "ok";
	}

	@PostMapping(value = "/record.jsp", produces = { MediaType.TEXT_PLAIN_VALUE })
	public String receiveVoiceRecord(@RequestParam Map<String, String> q, @RequestBody byte[] record) throws Exception {
		logger.info("Rabbit {} sent record", q.get("sn"));

		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("record.wav")));
		stream.write(record);
		stream.close();

		// bunnyService.handleRFIDTag(q.get("t"));
		return "ok";
	}

	@GetMapping(value = "/rfid.jsp", produces = { MediaType.TEXT_PLAIN_VALUE })
	public String receiveRFIDTag(@RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit {} found RFID tag {}", q.get("sn"), q.get("t"));
		bunnyService.handleRFIDTag(q.get("t"));

		return "ok";
	}
}