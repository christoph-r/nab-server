package at.vienna.glg.interfaces;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	/**
	 * Send initial boot image binary.
	 * 
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/bc.jsp", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public InputStreamResource sendBootImage(@RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit MAC {} with version {} started", q.get("m"), q.get("v"));
		return new InputStreamResource(bunnyService.getBootImage());
	}

	/**
	 * Sends ping and broadcast IPs.
	 * 
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/locate.jsp", produces = { MediaType.TEXT_PLAIN_VALUE })
	public String sendIPLocations(@RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit {} got located", q.get("sn"));
		String myIp = bunnyService.getLocalIp4Address();
		String resp = "ping " + myIp + "\n";
		resp += "broad " + myIp;
		return resp;
	}

	/**
	 * Periodically receives updates of actions and responds with new commands.
	 * 
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/p4.jsp", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public byte[] recieveUpdatesAndSendCommands(@RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit {} sent status {}", q.get("sn"), q.get("sd"));

		return toResponse(bunnyService.getNextCommand().generatePacket());
	}

	/**
	 * Receives recorded voice records.
	 * 
	 * @param q
	 * @param record
	 * @throws Exception
	 */
	@PostMapping(value = "/record.jsp")
	public void receiveRecord(@RequestParam Map<String, String> q, @RequestBody byte[] record) throws Exception {
		logger.info("Rabbit {} sent record", q.get("sn"));
		bunnyService.saveRecord(record);
	}

	/**
	 * Receives scanned RFID id.
	 * 
	 * @param q
	 * @throws Exception
	 */
	@GetMapping(value = "/rfid.jsp")
	public void receiveRFIDTag(@RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit {} found RFID tag {}", q.get("sn"), q.get("t"));
		bunnyService.handleRFIDTag(q.get("t"));
	}

	/**
	 * Sends choreography requested by given cId.
	 * 
	 * @param cId
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/choreos/{cId}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public byte[] sendChoreography(@PathVariable String cId, @RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit requested choreography {}", cId);
		return toResponse(bunnyService.getChoreography(cId));
	}

	/**
	 * Return record stream (mp3, wav) from requested record id.
	 * 
	 * @param rId
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/records/{rId:.+}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public InputStreamResource sendRecord(@PathVariable String rId, @RequestParam Map<String, String> q) throws Exception {
		logger.info("Rabbit requested record {}", rId);
		return new InputStreamResource(bunnyService.loadRecord(rId));
	}

	private byte[] toResponse(int[] data) {
		byte[] resp = new byte[data.length];
		for (int i = 0; i < resp.length; i++) {
			resp[i] = (byte) data[i];
		}

		return resp;
	}
}