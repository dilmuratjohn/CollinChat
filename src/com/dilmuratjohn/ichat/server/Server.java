package com.dilmuratjohn.ichat.server;

import com.dilmuratjohn.ichat.Prefix;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Server implements Runnable {

    private List<ServerClient> clients = new ArrayList<ServerClient>();
    private DatagramSocket socket;
    private final int MAX_RECEIVE_BYTES = 1024;
    private final int MAX_ATTEMPTS = 5;
    private final int port;
    private boolean running = false;
    private boolean raw = false;

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
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String command = scanner.nextLine();
            if (command.equals("/end all.")) {
                System.exit(-1);
            }
            if (command.equals("/clients")) {
                System.out.println("total: " + clients.size());
                for (ServerClient client : clients) {
                    System.out.println("> [" + client.getName() + "] (" + client.getAddress() + ":" + client.getPort() + ") {" + client.getId() + "}");
                }
            }
            if (command.equals("/raw")) {
                this.raw = !raw;
            }
            if (command.startsWith("/m/")) {
                sendToAll(command);
            }
        }
    }

    private void manageClients() {
        Thread manage = new Thread("manage") {
            public void run() {
                while (running) {
                    sendToAll(Prefix.PING.toString());
                    try {
                        sleep(2000);
                        for (int i = 0; i < clients.size(); i++) {
                            if (clients.get(i).getAttempt() > MAX_ATTEMPTS)
                                disconnect(clients.get(i).getId(), true);
                            else
                                clients.get(i).increaseAttempt();
                        }
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
        if (raw) System.out.println(message);
        if (message.startsWith(Prefix.CONNECTION.toString())) {
            UUID id = UUID.randomUUID();
            clients.add(new ServerClient(message.substring(Prefix.CONNECTION.toString().length()), packet.getAddress(), packet.getPort(), id));
            System.out.println("[" + message.substring(Prefix.CONNECTION.toString().length()) + "] in");
            message = Prefix.CONNECTION.toString() + id;
            send(message.getBytes(), packet.getAddress(), packet.getPort());
        } else if (message.startsWith(Prefix.MESSAGE.toString())) {
            sendToAll(message);
        } else if (message.startsWith(Prefix.DISCONNECTION.toString())) {
            disconnect(message.substring(Prefix.DISCONNECTION.toString().length()), false);
        } else if (message.startsWith(Prefix.PING.toString())) {
            reduceAttempt(message.substring(Prefix.PING.toString().length()));
        } else {
            System.out.println(message);
        }
    }

    private void disconnect(String id, boolean timeout) {
        for (ServerClient client : clients) {
            if (client.getId().equals(id)) {
                clients.remove(client);
                if (timeout)
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
        if (raw) System.out.println(message);
        for (ServerClient client : clients) {
            send(message.getBytes(), client.getAddress(), client.getPort());
        }
    }

    private void reduceAttempt(String message) {
        for (ServerClient client : clients) {
            if (client.getId().equals(message)) {
                client.setAttemptZero();
            }
        }
    }
}
