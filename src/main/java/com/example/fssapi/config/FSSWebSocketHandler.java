package com.example.fssapi.config;

import com.example.fssapi.model.ActionBasedPayload;
import com.example.fssapi.model.JoinPayload;
import com.example.fssapi.model.FSSAction;
import com.example.fssapi.exception.ClientInformingException;
import com.example.fssapi.model.TransferRequestPayload;
import com.example.fssapi.model.TransferResponsePayload;
import com.example.fssapi.validation.JakartaValidationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Objects;

@Slf4j
public abstract class FSSWebSocketHandler extends AbstractWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        try {
            JsonNode rootNode = objectMapper.readTree(payload);
            FSSAction action = FSSAction.valueOf(rootNode.get(ActionBasedPayload.Fields.action).asText());
            ActionBasedPayload validatedPayload = getValidatedPayload(action, payload);
            switch (action) {
                case JOIN:
                    handleJoin(session, (JoinPayload) validatedPayload);
                    break;
                case LEAVE:
                    handleLeave(session);
                    break;
                case REQUEST_TRANSFER:
                    handleTransferRequest(session, (TransferRequestPayload) validatedPayload);
                    break;
                case RESPOND_TO_TRANSFER:
                    handleTransferResponse(session, (TransferResponsePayload) validatedPayload);
                    break;
                default:
                    session.sendMessage(new TextMessage("invalid action"));
                    session.close();
            }
        } catch (ClientInformingException clientInformingException) {
            session.sendMessage(new TextMessage(clientInformingException.getMessage()));
            session.close();
        } catch (Exception exception) {
            log.error("error parsing received message, closing connection", exception);
            session.close();
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        System.out.println("test");
    }

    public ActionBasedPayload getValidatedPayload(FSSAction action, String payload) {
        ActionBasedPayload actionBasedPayload = getParsedPayload(action, payload);
        JakartaValidationUtil.throwIfViolations(actionBasedPayload);
        return actionBasedPayload;
    }

    public ActionBasedPayload getParsedPayload(FSSAction action, String payload) {
        Objects.requireNonNull(action, "Action cannot be null");
        Class<? extends ActionBasedPayload> targetClass = action.getPayloadClass();
        if (targetClass == null) {
            return null;
        }

        try {
            return objectMapper.readValue(payload, targetClass);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("error parsing received message", jsonProcessingException);
            throw new ClientInformingException("invalid payload");
        }
    }

    protected abstract void handleJoin(WebSocketSession session, JoinPayload joinMessage);

    protected abstract void handleLeave(WebSocketSession session);

    protected abstract void handleTransferRequest(WebSocketSession session, TransferRequestPayload transferRequestPayload);

    protected abstract void handleTransferResponse(WebSocketSession session, TransferResponsePayload transferResponsePayload);
}
