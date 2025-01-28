package com.example.fssapi.service;

import com.example.fssapi.persistence.entity.PeerEntity;
import com.example.fssapi.persistence.entity.TransferEntity;
import com.example.fssapi.persistence.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;

    public TransferEntity create(PeerEntity sender, PeerEntity receiver, String filename, Long filesize) {
        TransferEntity transferEntity = new TransferEntity(sender, receiver, filename, filesize, null);
        return transferRepository.save(transferEntity);
    }

    public TransferEntity setResponse(TransferEntity transferEntity, boolean accepted) {
        transferEntity.setAccepted(accepted);
        return transferRepository.save(transferEntity);
    }

    public TransferEntity findById(UUID transferId) {
        return transferRepository.findById(transferId).orElseThrow();
    }
}
