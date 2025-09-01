package com.sporty.feeds.bus;

import com.sporty.feeds.model.StandardMessage;

public interface MessageBus {
    void send(StandardMessage message);
}
