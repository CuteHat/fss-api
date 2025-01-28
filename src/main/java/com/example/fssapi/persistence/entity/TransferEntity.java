package com.example.fssapi.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false, foreignKey = @ForeignKey(name = "fk_transfer_sender"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PeerEntity sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false, foreignKey = @ForeignKey(name = "fk_transfer_receiver"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PeerEntity receiver;
    private String filename;
    private Long filesize;
    @Setter
    private Boolean accepted;

    public TransferEntity(PeerEntity sender,
                          PeerEntity receiver,
                          String filename,
                          long filesize,
                          Boolean accepted) {
        this.sender = sender;
        this.receiver = receiver;
        this.filename = filename;
        this.filesize = filesize;
        this.accepted = accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransferEntity transferEntity = (TransferEntity) o;
        return Objects.equals(id, transferEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
