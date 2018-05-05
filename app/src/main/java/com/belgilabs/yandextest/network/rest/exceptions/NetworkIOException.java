package com.belgilabs.yandextest.network.rest.exceptions;

import java.io.IOException;

public class NetworkIOException extends IOException {
    public NetworkIOException(Throwable ex) {
        super(ex);
    }
}

