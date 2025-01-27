package com.example.fssapi.model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class ActionBasedPayload {
    private FSSAction action;
}