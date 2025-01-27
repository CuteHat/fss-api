package com.example.fssapi.model;

import lombok.Getter;

@Getter
public enum FSSAction {
    JOIN(JoinPayload.class),
    LEAVE(null),
    REQUEST_TRANSFER(TransferRequestPayload.class),
    RESPOND_TO_TRANSFER(TransferResponsePayload.class);

    private final Class<? extends ActionBasedPayload> payloadClass;

    FSSAction(Class<? extends ActionBasedPayload> payloadClass) {
        this.payloadClass = payloadClass;
    }
}