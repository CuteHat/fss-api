package com.example.fssapi.model;

import com.example.fssapi.persistence.entity.TransferEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseNotification {
    private UUID id;
    private PeerDTO receiver;
    private boolean accepted;

    public static TransferResponseNotification fromEntity(TransferEntity entity) {
        return new TransferResponseNotification(entity.getId(), PeerDTO.from(entity.getReceiver()), entity.getAccepted());
    }
}
