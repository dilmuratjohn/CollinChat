package com.dilmuratjohn.ichat.server;

import java.net.InetAddress;

public class ServerClient {

    public String mName;
    public InetAddress mAddress;
    public int mPort;
    private final int mID;
    public int mAttempt = 0;

    public ServerClient(String name, InetAddress address, int port, final int ID){
        mName = name;
        mAddress = address;
        mPort = port;
        mID = ID;
    }

    public int getmID() {
        return mID;
    }
}
