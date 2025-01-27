package com.example.fssapi.persistence.repository;

import com.example.fssapi.persistence.entity.Transfer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class TransferRepository {
    private final Map<UUID, Transfer> transfers = new ConcurrentHashMap<>();

    public void addTransfer(Transfer transfer) {
        this.transfers.put(transfer.getTransferId(), transfer);
    }

    public void removeTransfer(Transfer transfer) {
        this.transfers.remove(transfer.getTransferId());
    }

    public void updateTransfer(Transfer transfer) {
        this.transfers.put(transfer.getTransferId(), transfer);
    }

    public Transfer getTransferById(UUID id) {
        return transfers.get(id);
    }
}
