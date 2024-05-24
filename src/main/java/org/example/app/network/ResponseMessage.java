package org.example.app.network;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    NO_CONTENT("No Content."),
    NOT_FOUND("Not Found."),
    DELETED("Deleted.");

    ResponseMessage(String resMsg) {
    }
}
