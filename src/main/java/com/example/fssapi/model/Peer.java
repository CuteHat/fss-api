package com.example.fssapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
public class Peer {
    private UUID id;
    private String nickname;
    private ZonedDateTime joinedAt;
    private ZonedDateTime lastSeenAt;

    public Peer(String nickname) {
        this(UUID.randomUUID(), nickname, ZonedDateTime.now(), ZonedDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Peer peer = (Peer) o;
        return Objects.equals(id, peer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
