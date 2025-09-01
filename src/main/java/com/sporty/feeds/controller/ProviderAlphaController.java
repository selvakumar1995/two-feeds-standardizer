package com.sporty.feeds.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sporty.feeds.bus.InMemoryMessageBus;
import com.sporty.feeds.model.StandardMessage;
import com.sporty.feeds.service.NormalizationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider-alpha")
public class ProviderAlphaController {

    private final NormalizationService normalizationService;
    private final InMemoryMessageBus bus;

    public ProviderAlphaController(NormalizationService normalizationService, InMemoryMessageBus bus) {
        this.normalizationService = normalizationService;
        this.bus = bus;
    }

    @PostMapping(path = "/feed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardMessage> receive(@RequestBody JsonNode payload) {
        StandardMessage msg = normalizationService.normalizeProviderAlpha(payload);
        bus.send(msg);
        return ResponseEntity.ok(msg);
    }
}
