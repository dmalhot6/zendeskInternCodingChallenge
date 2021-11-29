package com.zendesk.coding.challenge.mobileticketviewer;

import lombok.Data;

@Data
public class ResponseNotFoundException extends Exception{

    private final String message;

    public ResponseNotFoundException(String message) {

        this.message = message;
    }
}
