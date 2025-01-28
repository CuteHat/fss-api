package com.example.fssapi.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

@UtilityClass
public class SessionUtils {
    public UUID extractPeerFromSession(WebSocketSession session) {
        Object peerIdObject = session.getAttributes().get(LobbyUserProperties.PEER_ID.name());
        if (peerIdObject == null) {
            throw new IllegalArgumentException("Peer ID not found");
        }
        return (UUID) peerIdObject;
    }
}
