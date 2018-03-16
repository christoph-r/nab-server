package at.vienna.glg.services;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.uta.cs.nabaztag.Packet;

@Component
public class MessageQueue {
	private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);

	private final Queue<Packet> messages = new ConcurrentLinkedQueue<>();

	public boolean hasNext() {
		return !messages.isEmpty();
	}

	public Packet next() {
		return messages.poll();
	}

	public void push(Packet command) {
		messages.offer(command);
	}
}