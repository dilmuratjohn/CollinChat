package com.dilmuratjohn.ichat.server;

public class ServerMain {

    private int mPort;

    public ServerMain(int port) {
        mPort = port;
        System.out.println(port);
    }

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Usage: java -jar CollinChatServer.jar [port]");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new ServerMain(port);
    }
}
