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
    private DatagramSocket socket;
    private final int MAX_RECEIVE_BYTES = 1024;
    private final int port;
    private boolean running = false;

    public Server(final int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(this.port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        new Thread(this, "server").start();
    }

    public void run() {
        running = true;
        System.out.println("Server listening on port [" + port + "] ...");
        manageClients();
        receive();
    }

    private void manageClients() {
        Thread manage = new Thread("manage") {
            public void run() {
                while (running) {
                    try {
                        sleep(5000);
                        System.out.println("current client total: " + clients.size());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                        socket.receive(packet);
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
            System.out.println("[" + message.substring(Prefix.CONNECTION.toString().length()) + "] in");
            message = Prefix.CONNECTION.toString() + id;
            send(message.getBytes(), packet.getAddress(), packet.getPort());
        } else if (message.startsWith(Prefix.MESSAGE.toString())) {
            System.out.println(packet.getAddress() + ":" + packet.getPort());
            sendToAll(message);
        } else if (message.startsWith(Prefix.DISCONNECTION.toString())) {
            disconnect(message.substring(Prefix.DISCONNECTION.toString().length()), false);
        } else {
            System.out.println(message);
        }
    }

    private void disconnect(String data, boolean timeOut) {
        for (ServerClient client : clients) {
            if (client.getId().equals(data)) {
                clients.remove(client);
                if (timeOut)
                    System.out.println("[" + client.getName() + "] times out");
                else
                    System.out.println("[" + client.getName() + "] left");
                break;
            }
        }
    }

    private void send(final byte[] data, final InetAddress address, final int port) {
        Thread send = new Thread("send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                try {
                    socket.send(packet);
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
