package com.example.fssapi.model;

import com.example.fssapi.persistence.entity.Peer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PeerDTO {
    private UUID id;
    private String nickname;

    public static PeerDTO from(Peer peer) {
        return new PeerDTO(peer.getId(), peer.getNickname());
    }
}
