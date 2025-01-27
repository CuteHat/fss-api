package com.example.fssapi.service;

import com.example.fssapi.exception.ClientInformingException;
import com.example.fssapi.model.PeerDTO;
import com.example.fssapi.model.TransferNotificationDTO;
import com.example.fssapi.model.TransferRequestPayload;
import com.example.fssapi.persistence.entity.Peer;
import com.example.fssapi.util.LobbyUserProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceFacade {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketConnectionManager connectionManager;
    private final TransferService transferService;
    private final LobbyService lobbyService;

    public void initiateTransfer(TransferRequestPayload requestPayload, WebSocketSession session) {
        UUID senderId = extractSender(session);

        if (!lobbyService.isConnectedById(senderId)) {
            throw new ClientInformingException("Connect to lobby first");
        }
        Peer senderPeer = lobbyService.getById(senderId);

        if (!lobbyService.isConnectedById(requestPayload.getDestinationId())) {
            throw new ClientInformingException("Destination is not connected to the lobby");
        }

        if (!connectionManager.peerIsConnectedById(requestPayload.getDestinationId())) {
            throw new ClientInformingException("Destination is not connected");
        }

        transferService.create(senderId, requestPayload.getDestinationId());

        WebSocketSession destinationSession = connectionManager.getSessionByPeerId(requestPayload.getDestinationId());
        TransferNotificationDTO transferDTO = new TransferNotificationDTO(senderId, senderPeer.getNickname(), requestPayload.getFilename(), requestPayload.getSize());
        try {
            String serializedMessage = objectMapper.writeValueAsString(transferDTO);
            destinationSession.sendMessage(new TextMessage(serializedMessage, true));
        } catch (JsonProcessingException e) {
            log.error("unable to serialize transferDTO", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("unable to send message to destination", e);
            throw new RuntimeException(e);
        }
    }

    public UUID extractSender(WebSocketSession session) {
        Object peerIdObject = session.getAttributes().get(LobbyUserProperties.PEER_ID.name());
        if (peerIdObject == null) {
            throw new IllegalArgumentException("Peer ID not found");
        }
        return (UUID) peerIdObject;
    }
}
