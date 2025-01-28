package com.example.fssapi.model;

import com.example.fssapi.persistence.entity.TransferEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestNotification {
    private UUID id;
    private PeerDTO sender;
    private String fileName;
    private Long size;

    public static TransferRequestNotification from(TransferEntity entity) {
        return new TransferRequestNotification(
                entity.getId(),
                PeerDTO.from(entity.getSender()),
                entity.getFilename(),
                entity.getFilesize()
        );
    }
}
