package com.example.fssapi.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TransferRequestPayload extends ActionBasedPayload {
    @NotNull
    private UUID destinationId;
    @NotEmpty
    private String filename;
    @NotNull
    private Long filesize; // in bytes

    public TransferRequestPayload(UUID destinationId, String filename, Long filesize) {
        this.setAction(FSSAction.REQUEST_TRANSFER);
        this.destinationId = destinationId;
        this.filename = filename;
        this.filesize = filesize;
    }

}