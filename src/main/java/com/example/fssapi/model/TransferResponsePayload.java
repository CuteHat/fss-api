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

    public TransferResponsePayload(FSSAction fssAction, UUID transferId) {
        this.setAction(fssAction);
        this.transferId = transferId;
    }

    public void setAction(@NotNull FSSAction fssAction) {
        if (!fssAction.name().contains("TRANSFER")) {
            throw new IllegalArgumentException("Invalid fssAction");
        }
        super.setAction(fssAction);
    }
}