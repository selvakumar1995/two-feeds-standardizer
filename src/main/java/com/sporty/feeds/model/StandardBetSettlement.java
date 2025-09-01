package com.sporty.feeds.model;

public class StandardBetSettlement implements StandardMessage {
    private final String messageType = "BET_SETTLEMENT";
    private String eventId;
    private String outcome; // home | draw | away

    public StandardBetSettlement() {}

    public StandardBetSettlement(String eventId, String outcome) {
        this.eventId = eventId;
        this.outcome = outcome;
    }

    public String getMessageType() { return messageType; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
}
