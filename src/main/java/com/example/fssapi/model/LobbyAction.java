package com.example.fssapi.model;

import lombok.Getter;

@Getter
public enum LobbyAction {
    JOIN(JoinPayload.class),
    LEAVE(null);

    private final Class<? extends ActionBasedPayload> payloadClass;

    LobbyAction(Class<? extends ActionBasedPayload> payloadClass) {
        this.payloadClass = payloadClass;
    }
}