package com.dilmuratjohn.ichat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server implements Runnable {

    private List<ServerClient> clients = new ArrayList<ServerClient>();

    private int mPort;
    private DatagramSocket mSocket;
    private boolean running = false;

    public Server(int port) {
        mPort = port;
        try {
            mSocket = new DatagramSocket(mPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        new Thread(this, "server").start();
    }

    public void run() {
        running = true;
        System.out.println("Server listening on port [" + mPort + "] ...");
        manageClients();
        receive();
    }

    private void manageClients() {
        Thread manage = new Thread("manage") {
            public void run() {
                while (running) {
                    // TODO managing
                }
            }
        };
        manage.start();
    }

    private void receive() {
        Thread receive = new Thread("receive") {
            public void run() {
                while (running) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        mSocket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    process(packet);
                }
            }
        };
        receive.start();
    }

    private void process(DatagramPacket packet) {
        String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
        if (message.startsWith("/c/")) {
            UUID id = UUID.randomUUID();
            clients.add(new ServerClient(message.substring(3), packet.getAddress(), packet.getPort(), id));
            for (ServerClient client : clients) {
                System.out.println(client.getName() + "logged in");
                System.out.println(client.getAddress() + ":" + client.getPort());
            }
        } else {
            System.out.println(message);
        }
    }
}
