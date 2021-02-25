package br.com.urbainski.apipessoas.exception.handler;

import java.util.List;

import lombok.Getter;

@Getter
public class RestMessage {

    private String message;
    private List<String> messages;

    private RestMessage(List<String> messages) {

        this.messages = messages;
    }

    private RestMessage(String message) {

        this.message = message;
    }

    private RestMessage(String message, List<String> messages) {

        this.message = message;
        this.messages = messages;
    }

    public static RestMessage of(String message) {

        return new RestMessage(message);
    }

}