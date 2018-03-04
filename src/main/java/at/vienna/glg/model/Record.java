package at.vienna.glg.model;

import fi.uta.cs.nabaztag.MessageBlock;

public class Record extends MessageBlock {
	public Record(String record, boolean local) {

		if (local) {
			this.encode("MU broadcast/vl/records/" + record + "\nMW\n");
		} else {
			this.encode("MU " + record + "\nMW\n");
		}
	}
}
