package com.example.fssapi.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
public class Transfer {
    private UUID transferId;
    private UUID sender;
    private UUID receiver;
    @Setter
    private Boolean accepted;

    public Transfer(UUID sender, UUID receiver, Boolean accepted) {
        this.transferId = UUID.randomUUID();
        this.sender = sender;
        this.receiver = receiver;
        this.accepted = accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(transferId, transfer.transferId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transferId);
    }
}
