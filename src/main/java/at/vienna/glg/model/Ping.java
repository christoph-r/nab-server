package at.vienna.glg.model;

import fi.uta.cs.nabaztag.PingIntervalBlock;

public class Ping extends PingIntervalBlock {

	public Ping() {
		super(60);
	}

	public Ping(int length) {
		super(length);
	}
}