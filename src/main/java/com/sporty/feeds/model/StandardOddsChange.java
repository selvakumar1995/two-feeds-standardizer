package com.sporty.feeds.model;

public class StandardOddsChange implements StandardMessage {
    private final String messageType = "ODDS_CHANGE";
    private String eventId;
    private double home;
    private double draw;
    private double away;

    public StandardOddsChange() {}

    public StandardOddsChange(String eventId, double home, double draw, double away) {
        this.eventId = eventId;
        this.home = home;
        this.draw = draw;
        this.away = away;
    }

    public String getMessageType() { return messageType; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public double getHome() { return home; }
    public void setHome(double home) { this.home = home; }
    public double getDraw() { return draw; }
    public void setDraw(double draw) { this.draw = draw; }
    public double getAway() { return away; }
    public void setAway(double away) { this.away = away; }
}
