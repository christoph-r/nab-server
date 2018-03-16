package at.vienna.glg.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.vienna.glg.model.Choreo;
import at.vienna.glg.model.PresenceStatus;
import at.vienna.glg.services.MessageQueue;
import fi.uta.cs.nabaztag.Packet;

/**
 * Handles requests from openhab2.
 */
@RestController
@RequestMapping("oh/")
public class OHController {
	private static final Logger logger = LoggerFactory.getLogger(OHController.class);

	@Autowired
	private MessageQueue queue;

	@PostMapping(value = "/presence", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void updatePresence(@RequestBody PresenceStatus update) throws Exception {
		logger.info("Presence updated for {} is online {} ", update.getPerson(), update.isOnline());
		String choreoId = update.getPerson();
		choreoId += update.isOnline() ? ":online" : ":offline";

		Packet p = new Packet();
		p.addBlock(new Choreo(choreoId));
		queue.push(p);
	}
}