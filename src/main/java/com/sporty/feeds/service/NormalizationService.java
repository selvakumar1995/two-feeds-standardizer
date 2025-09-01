package com.sporty.feeds.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sporty.feeds.exception.BadFeedRequestException;
import com.sporty.feeds.model.StandardBetSettlement;
import com.sporty.feeds.model.StandardMessage;
import com.sporty.feeds.model.StandardOddsChange;
import org.springframework.stereotype.Service;

@Service
public class NormalizationService {

    public StandardMessage normalizeProviderAlpha(JsonNode payload) {
        if (payload == null || !payload.hasNonNull("msg_type") || !payload.hasNonNull("event_id")) {
            throw new BadFeedRequestException("ProviderAlpha: 'msg_type' and 'event_id' are required");
        }
        String msgType = payload.get("msg_type").asText();
        String eventId = payload.get("event_id").asText();

        if ("odds_update".equals(msgType)) {
            JsonNode values = payload.get("values");
            if (values == null) {
                throw new BadFeedRequestException("ProviderAlpha ODDS: 'values' is required");
            }
            double home = reqNumber(values, "1");
            double draw = reqNumber(values, "X");
            double away = reqNumber(values, "2");
            return new StandardOddsChange(eventId, home, draw, away);
        } else if ("settlement".equals(msgType)) {
            String outcomeCode = reqText(payload, "outcome");
            String out;
            if ("1".equals(outcomeCode)) out = "home";
            else if ("X".equals(outcomeCode)) out = "draw";
            else if ("2".equals(outcomeCode)) out = "away";
            else throw new BadFeedRequestException("ProviderAlpha SETTLEMENT: invalid outcome '" + outcomeCode + "'");
            return new StandardBetSettlement(eventId, out);
        }
        throw new BadFeedRequestException("ProviderAlpha: Unknown msg_type '" + msgType + "'");
    }

    public StandardMessage normalizeProviderBeta(JsonNode payload) {
        if (payload == null || !payload.hasNonNull("type") || !payload.hasNonNull("event_id")) {
            throw new BadFeedRequestException("ProviderBeta: 'type' and 'event_id' are required");
        }
        String type = payload.get("type").asText();
        String eventId = payload.get("event_id").asText();

        if ("ODDS".equals(type)) {
            JsonNode odds = payload.get("odds");
            if (odds == null) {
                throw new BadFeedRequestException("ProviderBeta ODDS: 'odds' is required");
            }
            double home = reqNumber(odds, "home");
            double draw = reqNumber(odds, "draw");
            double away = reqNumber(odds, "away");
            return new StandardOddsChange(eventId, home, draw, away);
        } else if ("SETTLEMENT".equals(type)) {
            String result = reqText(payload, "result");
            if (!"home".equals(result) && !"draw".equals(result) && !"away".equals(result)) {
                throw new BadFeedRequestException("ProviderBeta SETTLEMENT: invalid result '" + result + "'");
            }
            return new StandardBetSettlement(eventId, result);
        }
        throw new BadFeedRequestException("ProviderBeta: Unknown type '" + type + "'");
    }

    private static String reqText(JsonNode node, String field) {
        if (!node.hasNonNull(field)) {
            throw new BadFeedRequestException("Missing required field '" + field + "'");
        }
        return node.get(field).asText();
    }

    private static double reqNumber(JsonNode node, String field) {
        if (!node.hasNonNull(field) || !node.get(field).isNumber()) {
            throw new BadFeedRequestException("Missing or non-numeric field '" + field + "'");
        }
        return node.get(field).asDouble();
    }
}
