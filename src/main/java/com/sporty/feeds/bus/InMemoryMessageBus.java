package com.sporty.feeds.bus;

import com.sporty.feeds.model.StandardMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class InMemoryMessageBus implements MessageBus {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMessageBus.class);
    private final LinkedList<StandardMessage> queue = new LinkedList<StandardMessage>();

    public synchronized void send(StandardMessage message) {
        log.info("Published standardized message: {}", message);
        queue.addLast(message);
        if (queue.size() > 1000) {
            queue.removeFirst();
        }
    }

    public synchronized List<StandardMessage> snapshot() {
        return Collections.unmodifiableList(new LinkedList<StandardMessage>(queue));
    }
}
