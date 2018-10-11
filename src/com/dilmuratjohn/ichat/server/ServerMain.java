package com.dilmuratjohn.ichat.server;

public class ServerMain {

    private int mPort;
    private Server mServer;

    public ServerMain(int port) {
        mPort = port;
        mServer = new Server(mPort);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar CollinChatServer.jar [port]");
            return;
        }
        new ServerMain(Integer.parseInt(args[0]));
    }
}
