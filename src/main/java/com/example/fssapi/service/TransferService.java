package com.example.fssapi.service;

import com.example.fssapi.persistence.entity.Transfer;
import com.example.fssapi.persistence.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;

    public void create(UUID sender, UUID receiver) {
        transferRepository.addTransfer(new Transfer(sender, receiver, null));
    }

    public void setResponse(UUID transferId, UUID receiver, boolean accepted) {
        Transfer foundTransfer = transferRepository.getTransferById(transferId);
        if (foundTransfer == null) {
            throw new IllegalArgumentException("Transfer not found");
        }
        if (!foundTransfer.getReceiver().equals(receiver)) {
            throw new IllegalArgumentException("Transfer receiver doesn't match");
        }
        foundTransfer.setAccepted(accepted);
    }
}
