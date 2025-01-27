package com.example.fssapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferNotificationDTO {
    private UUID senderId;
    private String sender;
    private String fileName;
    private Long size;
}
