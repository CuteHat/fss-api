package com.example.fssapi.model;

import com.example.fssapi.persistence.entity.PeerEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PeerDTO {
    private UUID id;
    private String nickname;

    public static PeerDTO from(PeerEntity peer) {
        return new PeerDTO(peer.getId(), peer.getNickname());
    }
}
