package com.example.fssapi.service;

import com.example.fssapi.persistence.entity.Peer;
import com.example.fssapi.persistence.repository.LobbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LobbyService {
    @Value("${peer.lobby.limit}")
    private int lobbyLimit;
    private final LobbyRepository lobbyRepository;

    public Peer join(String nickname) {
        if (lobbyRepository.getCurrentSize() == lobbyLimit) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Lobby is currently full. Please try again later.");
        }

        Peer candidate = new Peer(nickname);
        if (lobbyRepository.contains(candidate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Peer already joined");
        }

        lobbyRepository.add(candidate);
        return candidate;
    }

    public void leave(UUID id) {
        lobbyRepository.removeById(id);
    }

    public Set<Peer> getConnectedPeers() {
        return lobbyRepository.getConnectedPeers();
    }

    public boolean isConnectedById(UUID id) {
        return lobbyRepository.containsById(id);
    }

    public Peer getById(UUID id) {
        return lobbyRepository.getById(id);
    }
}
