package com.sporty.feeds.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.feeds.model.StandardBetSettlement;
import com.sporty.feeds.model.StandardMessage;
import com.sporty.feeds.model.StandardOddsChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalizationServiceTest {

    private NormalizationService normalizationService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        normalizationService = new NormalizationService();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testNormalizeProviderAlphaOdds() throws Exception {
        String json = "{ \"msg_type\": \"odds_update\", \"event_id\": \"ev123\", " +
                "\"values\": {\"1\": 2.0, \"X\": 3.1, \"2\": 3.8} }";
        JsonNode node = objectMapper.readTree(json);

        StandardMessage result = normalizationService.normalizeProviderAlpha(node);

        assertTrue(result instanceof StandardOddsChange, "Expected StandardOddsChange");
        StandardOddsChange odds = (StandardOddsChange) result;

        assertEquals("ODDS_CHANGE", odds.getMessageType());
        assertEquals("ev123", odds.getEventId());
        assertEquals(2.0, odds.getHome(), 1e-6);
        assertEquals(3.1, odds.getDraw(), 1e-6);
        assertEquals(3.8, odds.getAway(), 1e-6);
    }

    @Test
    void testNormalizeProviderAlphaSettlement() throws Exception {
        String json = "{ \"msg_type\": \"settlement\", \"event_id\": \"ev124\", \"outcome\": \"1\" }";
        JsonNode node = objectMapper.readTree(json);

        StandardMessage result = normalizationService.normalizeProviderAlpha(node);

        assertTrue(result instanceof StandardBetSettlement, "Expected StandardBetSettlement");
        StandardBetSettlement settlement = (StandardBetSettlement) result;

        assertEquals("BET_SETTLEMENT", settlement.getMessageType());
        assertEquals("ev124", settlement.getEventId());
        assertEquals("home", settlement.getOutcome());
    }

    @Test
    void testNormalizeProviderBetaOdds() throws Exception {
        String json = "{ \"type\": \"ODDS\", \"event_id\": \"ev200\", " +
                "\"odds\": {\"home\": 1.9, \"draw\": 3.0, \"away\": 4.2} }";
        JsonNode node = objectMapper.readTree(json);

        StandardMessage result = normalizationService.normalizeProviderBeta(node);

        assertTrue(result instanceof StandardOddsChange, "Expected StandardOddsChange");
        StandardOddsChange odds = (StandardOddsChange) result;

        assertEquals("ODDS_CHANGE", odds.getMessageType());
        assertEquals("ev200", odds.getEventId());
        assertEquals(1.9, odds.getHome(), 1e-6);
        assertEquals(3.0, odds.getDraw(), 1e-6);
        assertEquals(4.2, odds.getAway(), 1e-6);
    }

    @Test
    void testNormalizeProviderBetaSettlement() throws Exception {
        String json = "{ \"type\": \"SETTLEMENT\", \"event_id\": \"ev201\", \"result\": \"away\" }";
        JsonNode node = objectMapper.readTree(json);

        StandardMessage result = normalizationService.normalizeProviderBeta(node);

        assertTrue(result instanceof StandardBetSettlement, "Expected StandardBetSettlement");
        StandardBetSettlement settlement = (StandardBetSettlement) result;

        assertEquals("BET_SETTLEMENT", settlement.getMessageType());
        assertEquals("ev201", settlement.getEventId());
        assertEquals("away", settlement.getOutcome());
    }
}
