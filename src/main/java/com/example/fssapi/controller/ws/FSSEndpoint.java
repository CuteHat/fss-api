package com.example.fssapi.controller.ws;

import com.example.fssapi.config.FSSWebSocketHandler;
import com.example.fssapi.model.JoinPayload;
import com.example.fssapi.service.LobbyServiceFacade;
import com.example.fssapi.service.WebSocketConnectionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
@Slf4j
public class FSSEndpoint extends FSSWebSocketHandler {
    private final LobbyServiceFacade lobbyServiceFacade;
    private final WebSocketConnectionManager webSocketConnectionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("New connection established {}", session);
    }

    @Override
    protected void handleJoin(WebSocketSession session, JoinPayload joinMessage) {
        lobbyServiceFacade.handleJoin(joinMessage, session);
    }

    @Override
    protected void handleLeave(WebSocketSession session) {
        lobbyServiceFacade.handleLeave(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Transport error for session: {}", session, exception);
        handleLeave(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.debug("Connection closed for session:{} with status: {}", session, closeStatus);
        handleLeave(session);
    }
}
