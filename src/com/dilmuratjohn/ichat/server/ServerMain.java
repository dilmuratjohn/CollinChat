package com.dilmuratjohn.ichat.server;

public class ServerMain {

    private ServerMain(int port) {
        new Server(port);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar CollinChatServer.jar [port]");
            return;
        }
        new ServerMain(Integer.parseInt(args[0]));
    }
}
