package com.example.fssapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TransferResponsePayload extends ActionBasedPayload {

    @NotNull
    private UUID transferId;
    @NotNull
    private Boolean accept;

    public TransferResponsePayload(FSSAction fssAction, UUID transferId) {
        this.setAction(fssAction);
        this.transferId = transferId;
    }
}