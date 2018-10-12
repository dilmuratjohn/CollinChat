package com.dilmuratjohn.ichat.server;

import java.net.InetAddress;
import java.util.UUID;

class ServerClient {

    private final String name;
    private final InetAddress address;
    private final int port;
    private final String id;
    private int attempt = 0;

    ServerClient(String name, InetAddress address, int port, final UUID id) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.id = id.toString();
    }

    String getName() {
        return this.name;
    }

    String getId() {
        return this.id;
    }

    InetAddress getAddress() {
        return this.address;
    }

    int getPort() {
        return this.port;
    }

    void increaseAttempt() {
        ++this.attempt;
    }

    int getAttempt() {
        return this.attempt;
    }
}
