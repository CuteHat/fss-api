package com.example.fssapi.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class JoinPayload extends ActionBasedPayload {
    @NotEmpty
    private String nickname;

    public JoinPayload(String nickname) {
        this.setAction(FSSAction.JOIN);
        this.nickname = nickname;
    }

}