package at.vienna.glg.model;

import fi.uta.cs.nabaztag.MessageBlock;

public class Choreo extends MessageBlock {
	public Choreo(String choreo) {
		this.encode("CH broadcast/vl/choreos/" + choreo + "\nMW\n");
	}
}