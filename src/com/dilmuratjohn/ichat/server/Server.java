package com.dilmuratjohn.ichat.server;

import com.dilmuratjohn.ichat.Prefix;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server implements Runnable {

    private List<ServerClient> clients = new ArrayList<ServerClient>();

    private final int MAX_RECEIVE_BYTES = 1024;

    private int mPort;
    private DatagramSocket mSocket;
    private boolean running = false;

    public Server(final int port) {
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
                    byte[] data = new byte[MAX_RECEIVE_BYTES];
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

    private void process(final DatagramPacket packet) {
        String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
        if (message.startsWith(Prefix.CONNECTION.toString())) {
            UUID id = UUID.randomUUID();
            clients.add(new ServerClient(message.substring(Prefix.CONNECTION.toString().length()), packet.getAddress(), packet.getPort(), id));
            System.out.println(message.substring(Prefix.CONNECTION.toString().length()));
            String data = Prefix.CONNECTION.toString() + id;
            send(data.getBytes(), packet.getAddress(), packet.getPort());
        } else if (message.startsWith(Prefix.MESSAGE.toString())) {
            System.out.println(packet.getAddress() + ":" + packet.getPort());
            sendToAll(message);
        } else {
            System.out.println(message);
        }
    }

    private void send(final byte[] data, final InetAddress address, final int port) {
        Thread send = new Thread("send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                try {
                    mSocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    private void sendToAll(String message) {
        for (ServerClient client : clients) {
            send(message.getBytes(), client.getAddress(), client.getPort());
        }
    }
}
