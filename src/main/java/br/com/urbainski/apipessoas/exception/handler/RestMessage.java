package br.com.urbainski.apipessoas.exception.handler;

import java.util.Map;

import lombok.Getter;

@Getter
public class RestMessage {

    private String message;
    private Map<String, String> messages;

    private RestMessage(Map<String, String> messages) {

        this.messages = messages;
    }

    private RestMessage(String message) {

        this.message = message;
    }

    private RestMessage(String message, Map<String, String> messages) {

        this.message = message;
        this.messages = messages;
    }

    public static RestMessage of(String message) {

        return new RestMessage(message);
    }

    public static RestMessage of(Map<String, String> messages) {

        return new RestMessage(messages);
    }

}