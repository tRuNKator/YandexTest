package com.belgilabs.yandextest.network.rest.exceptions;

public class ServerIOException extends ServerException {

    public ServerIOException() {
        super();
    }

    public ServerIOException(String message) {
        super(message);
    }

    public ServerIOException(Throwable e) {
        super(e);
    }
}

