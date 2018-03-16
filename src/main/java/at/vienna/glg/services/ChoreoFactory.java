package at.vienna.glg.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.uta.cs.nabaztag.Choreography;

public class ChoreoFactory {
	private static final Logger logger = LoggerFactory.getLogger(ChoreoFactory.class);

	private static Map<String, Choreography> choreographies = new HashMap<>();

	private ChoreoFactory() {
		// unused
	}

	static {
		Choreography ch = new Choreography();
		ch.addEarMove(20, Choreography.EAR_RIGHT, 0, Choreography.DIRECTION_FORWARD);
		choreographies.put("ch:online", ch);

		ch = new Choreography();
		ch.addEarMove(20, Choreography.EAR_RIGHT, 9, Choreography.DIRECTION_FORWARD);
		choreographies.put("ch:offline", ch);

		ch = new Choreography();
		ch.addEarMove(20, Choreography.EAR_LEFT, 0, Choreography.DIRECTION_BACKWARD);
		choreographies.put("wu:online", ch);

		ch = new Choreography();
		ch.addEarMove(20, Choreography.EAR_LEFT, 9, Choreography.DIRECTION_BACKWARD);
		choreographies.put("wu:offline", ch);

		// cb.addEarMove(50, Choreography.EAR_RIGHT, rand.nextInt(13), rand.nextInt(2));
		// cb.addEarMove(10, Choreography.EAR_LEFT, rand.nextInt(13), rand.nextInt(2));
		// cb.addLedCommand(10, Choreography.LED_CENTER, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		// cb.addLedCommand(2, Choreography.LED_RIGHT, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		// cb.addLedCommand(3, Choreography.LED_LEFT, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		// cb.addLedCommand(20, Choreography.LED_BOTTOM, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		// cb.addLedCommand(30, Choreography.LED_TOP, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

	}

	public static Choreography getChoreography(String choreoId) throws Exception {
		return choreographies.get(choreoId);
	}
}