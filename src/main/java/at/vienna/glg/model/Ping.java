package at.vienna.glg.model;

import fi.uta.cs.nabaztag.PingIntervalBlock;

public class Ping extends PingIntervalBlock {

	/**
	 * Create default ping interval.
	 */
	public Ping() {
		super(60);
	}

	/**
	 * Create ping intervall in seconds.
	 * 
	 * @param interval
	 */
	public Ping(int interval) {
		super(interval);
	}
}