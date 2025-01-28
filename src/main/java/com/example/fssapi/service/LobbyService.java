package com.example.fssapi.service;

import com.example.fssapi.persistence.entity.PeerEntity;
import com.example.fssapi.persistence.repository.PeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LobbyService {
    @Value("${peer.lobby.limit}")
    private int lobbyLimit;
    private final PeerRepository lobbyRepository;

    public PeerEntity join(String nickname) {
        if (lobbyRepository.count() == lobbyLimit) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Lobby is currently full. Please try again later.");
        }

        PeerEntity peer = new PeerEntity();
        peer.setNickname(nickname);

        return lobbyRepository.save(peer);
    }

    public void leave(UUID id) {
        lobbyRepository.deleteById(id);
    }

    public List<PeerEntity> getConnectedPeers() {
        return lobbyRepository.findAll();
    }

    public boolean isConnectedById(UUID id) {
        return lobbyRepository.existsById(id);
    }

    public PeerEntity findById(UUID id) {
        return lobbyRepository.findById(id).orElseThrow();
    }
}
