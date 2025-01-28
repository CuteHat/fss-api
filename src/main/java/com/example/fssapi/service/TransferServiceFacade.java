package com.example.fssapi.service;

import com.example.fssapi.model.TransferRequestNotification;
import com.example.fssapi.model.TransferRequestPayload;
import com.example.fssapi.model.TransferResponseNotification;
import com.example.fssapi.model.TransferResponsePayload;
import com.example.fssapi.persistence.entity.PeerEntity;
import com.example.fssapi.persistence.entity.TransferEntity;
import com.example.fssapi.util.LobbyUserProperties;
import com.example.fssapi.util.SessionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void initiateTransfer(TransferRequestPayload requestPayload, WebSocketSession session) {
        UUID senderId = SessionUtils.extractPeerFromSession(session);
        PeerEntity senderPeer = lobbyService.findById(senderId);
        PeerEntity receiver = lobbyService.findById(requestPayload.getDestinationId());

        // check connectivity with destination
        connectionManager.getSessionByPeerId(requestPayload.getDestinationId());

        TransferEntity transferEntity = transferService.create(senderPeer, receiver, requestPayload.getFilename(), requestPayload.getFilesize());

        WebSocketSession destinationSession = connectionManager.getSessionByPeerId(requestPayload.getDestinationId());
        TransferRequestNotification transferDTO = TransferRequestNotification.from(transferEntity);
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

    // Maybe I should avoid connection checks here and instead check the connection only when sending actual data
    @Transactional
    public void handleTransferResponse(TransferResponsePayload responsePayload, WebSocketSession session) {
        TransferEntity transferEntity = transferService.findById(responsePayload.getTransferId());
        PeerEntity receiver = transferEntity.getReceiver();
        PeerEntity sender = transferEntity.getSender();
        if (!receiver.getId().equals(SessionUtils.extractPeerFromSession(session))) {
            throw new RuntimeException("Receiver does not belong to this transfer");
        }

        transferEntity = transferService.setResponse(transferEntity, responsePayload.getAccept());
        TransferResponseNotification transferResponseNotification = TransferResponseNotification.fromEntity(transferEntity);

        WebSocketSession senderSession = connectionManager.getSessionByPeerId(sender.getId());
        try {
            senderSession.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(transferResponseNotification)
            ));
        } catch (JsonProcessingException e) {
            log.error("unable to serialize transferDTO", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("unable to send message to destination", e);
            throw new RuntimeException(e);
        }
    }

}
