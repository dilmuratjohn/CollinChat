package com.dilmuratjohn.ichat.server;

import java.net.InetAddress;
import java.util.UUID;

class ServerClient {

    private String mName;
    private InetAddress mAddress;
    private int mPort;
    private final UUID mID;
    private int mAttempt = 0;

    ServerClient(String name, InetAddress address, int port, final UUID ID) {
        mName = name;
        mAddress = address;
        mPort = port;
        mID = ID;
    }

    String getName() {
        return mName;
    }

    InetAddress getAddress() {
        return mAddress;
    }

    int getPort() {
        return mPort;
    }
}
