package com.sporty.feeds.controller;

import com.sporty.feeds.bus.InMemoryMessageBus;
import com.sporty.feeds.model.StandardMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/debug")
public class DebugController {

    private final InMemoryMessageBus bus;

    public DebugController(InMemoryMessageBus bus) {
        this.bus = bus;
    }

    @GetMapping("/queue")
    public ResponseEntity<List<StandardMessage>> snapshot() {
        return ResponseEntity.ok(bus.snapshot());
    }
}
