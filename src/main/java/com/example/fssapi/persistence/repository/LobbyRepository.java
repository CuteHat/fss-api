package com.example.fssapi.persistence.repository;

import com.example.fssapi.persistence.entity.Peer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class LobbyRepository {
    @Getter
    private final Set<Peer> connectedPeers = new CopyOnWriteArraySet<>();

    public void add(Peer peer) {
        connectedPeers.add(peer);
    }

    public Set<Peer> getPeers() {
        return connectedPeers;
    }

    public Peer getById(UUID id) {
        for (Peer peer : connectedPeers) {
            if (peer.getId().equals(id)) {
                return peer;
            }
        }
        return null;
    }

    public int getCurrentSize() {
        return connectedPeers.size();
    }

    public void remove(Peer peer) {
        connectedPeers.remove(peer);
    }

    public void removeById(UUID id) {
        for (Peer peer : connectedPeers) {
            if (peer.getId().equals(id)) {
                remove(peer);
            }
        }
    }

    public boolean contains(Peer peer) {
        return connectedPeers.contains(peer);
    }

    public boolean containsById(UUID id) {
        return getById(id) != null;
    }
}
