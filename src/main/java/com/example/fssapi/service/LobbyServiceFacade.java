package com.example.fssapi.service;

import com.example.fssapi.model.JoinPayload;
import com.example.fssapi.persistence.entity.PeerEntity;
import com.example.fssapi.model.PeerDTO;
import com.example.fssapi.model.SSEEventName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.fssapi.util.LobbyUserProperties.CONNECTED_AT;
import static com.example.fssapi.util.LobbyUserProperties.PEER_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LobbyServiceFacade {
    private final LobbyService lobbyService;
    private final WebSocketConnectionManager connectionManager;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public void handleOpenConnection(WebSocketSession session) {
        session.getAttributes().put(CONNECTED_AT.name(), ZonedDateTime.now());
    }

    public void handleJoin(JoinPayload joinPayload, WebSocketSession session) {
        PeerEntity joinedPeer = lobbyService.join(joinPayload.getNickname());
        session.getAttributes().put(PEER_ID.name(), joinedPeer.getId());
        connectionManager.add(session);
    }

    public void handleLeave(WebSocketSession session) {
        if (session.getAttributes().containsKey(PEER_ID.name())) {
            log.debug("peer id found in session attributes, removing it from peer database");
            UUID id = (UUID) session.getAttributes().get(PEER_ID.name());
            lobbyService.leave(id);
        }
        connectionManager.remove(session);
        try {
            session.close();
        } catch (IOException e) {
            log.error("unable to close session", e);
        }
    }

    public SseEmitter getSseEmitterForLobby() {
        SseEmitter emitter = new SseEmitter();
        executorService.scheduleAtFixedRate(getEmitterRunnable(emitter), 0, 1, TimeUnit.SECONDS);
        return emitter;
    }

    private Runnable getEmitterRunnable(SseEmitter emitter) {
        return () -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(SSEEventName.GET_LOBBY.name())
                        .data(getPeerDtos())
                        .build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public List<PeerDTO> getPeerDtos() {
        return lobbyService
                .getConnectedPeers()
                .stream()
                .map(PeerDTO::from)
                .toList();
    }
}
