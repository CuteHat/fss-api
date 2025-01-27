package com.example.fssapi.controller;

import com.example.fssapi.service.LobbyServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/lobby")
@RequiredArgsConstructor
public class LobbyController {
    private final LobbyServiceFacade lobbyServiceFacade;

    @GetMapping
    public SseEmitter streamLobby() {
        return lobbyServiceFacade.getSseEmitterForLobby();
    }
}
