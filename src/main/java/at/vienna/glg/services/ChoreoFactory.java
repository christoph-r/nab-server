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
		/*
		 * 0 - ear up; 17 - 1 rotation + ear up; 10 - ear down
		 */
		ch.addEarMove(20, Choreography.EAR_RIGHT, 17, Choreography.DIRECTION_FORWARD);
		ch.addLedCommand(10, Choreography.LED_RIGHT, 0, 255, 0);
		choreographies.put("ch:online", ch);

		ch = new Choreography();
		ch.addEarMove(20, Choreography.EAR_RIGHT, 10, Choreography.DIRECTION_BACKWARD);
		ch.addLedCommand(10, Choreography.LED_RIGHT, 255, 0, 0);
		choreographies.put("ch:offline", ch);

		ch = new Choreography();
		ch.addEarMove(20, Choreography.EAR_LEFT, 0, Choreography.DIRECTION_FORWARD);
		ch.addLedCommand(10, Choreography.LED_LEFT, 0, 255, 0);
		choreographies.put("wu:online", ch);

		ch = new Choreography();
		ch.addEarMove(20, Choreography.EAR_LEFT, 10, Choreography.DIRECTION_BACKWARD);
		ch.addLedCommand(10, Choreography.LED_LEFT, 255, 0, 0);
		choreographies.put("wu:offline", ch);
	}

	public static Choreography getChoreography(String choreoId) throws Exception {
		return choreographies.get(choreoId);
	}
}