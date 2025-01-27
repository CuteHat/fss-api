package com.example.fssapi.service;

import com.example.fssapi.util.LobbyUserProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketConnectionManager {
    private final Map<UUID, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void add(WebSocketSession session) {
        UUID peerId = getPeerIdFrom(session);
        sessions.put(peerId, session);
    }

    public void remove(WebSocketSession session) {
        UUID peerId = getPeerIdFrom(session);
        sessions.remove(peerId);
    }

    public boolean peerIsConnectedById(UUID peerId) {
        return getSessionByPeerId(peerId) != null;
    }

    public WebSocketSession getSessionByPeerId(UUID peerId) {
        return sessions.get(peerId);
    }

    private static UUID getPeerIdFrom(WebSocketSession session) {
        Object peerIdObject = session.getAttributes().get(LobbyUserProperties.PEER_ID.name());
        if (peerIdObject == null) {
            throw new IllegalArgumentException("Peer ID not found");
        }
        return (UUID) peerIdObject;
    }
}
