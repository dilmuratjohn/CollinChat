package com.dilmuratjohn.ichat.server;

import com.dilmuratjohn.ichat.Globals;

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
            if (command.equals(Globals.Command.QUIT.toString())) {
                quit();
            } else if (command.equals(Globals.Command.SHOW_CLIENTS.toString())) {
                System.out.println("total: " + clients.size());
                for (ServerClient client : clients)
                    System.out.println("> [" + client.getName() + "] (" + client.getAddress() + ":" + client.getPort() + ") {" + client.getId() + "}");
            } else if (command.equals(Globals.Command.TOGGLE_RAW_MODE.toString())) {
                this.raw = !raw;
                command = raw ? "on" : "off";
                System.out.println("Row mode " + command + ".");
            } else if (command.startsWith(Globals.Command.HELP.toString())) {
                help();
            } else if (command.startsWith(Globals.Command.SEND_MESSAGE_TO_ALL.toString())) {
                sendToAll(command);
            } else if (command.startsWith(Globals.Command.KICK.toString())) {
                disconnect(command.substring(Globals.Command.KICK.toString().length()), Globals.Status.KICKED);
            } else {
                System.out.println("unknown commend.");
                System.out.println("there is a help for you:");
                help();
            }
            ;

        }
    }

    private void manageClients() {
        Thread manage = new Thread("manage") {
            public void run() {
                while (running) {
                    sendToAll(Globals.Prefix.PING.toString());
                    try {
                        sleep(2000);
                        String userList = "";
                        for (int i = 0; i < clients.size(); i++) {
                            if (clients.get(i).getAttempt() > MAX_ATTEMPTS)
                                disconnect(clients.get(i).getId(), Globals.Status.TIMEOUT);
                            else
                                clients.get(i).increaseAttempt();
                            userList = userList.concat(Globals.Prefix.ONLINE_USER.toString() + clients.get(i).getName());
                        }
                        sendToAll(userList);
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
        if (message.startsWith(Globals.Prefix.CONNECTION.toString())) {
            UUID id = UUID.randomUUID();
            clients.add(new ServerClient(message.substring(Globals.Prefix.CONNECTION.toString().length()), packet.getAddress(), packet.getPort(), id));
            System.out.println("[" + message.substring(Globals.Prefix.CONNECTION.toString().length()) + "] in");
            message = Globals.Prefix.CONNECTION.toString() + id;
            send(message.getBytes(), packet.getAddress(), packet.getPort());
        } else if (message.startsWith(Globals.Prefix.MESSAGE.toString())) {
            sendToAll(message);
        } else if (message.startsWith(Globals.Prefix.DISCONNECTION.toString())) {
            disconnect(message.substring(Globals.Prefix.DISCONNECTION.toString().length()), Globals.Status.LEFT);
        } else if (message.startsWith(Globals.Prefix.PING.toString())) {
            reduceAttempt(message.substring(Globals.Prefix.PING.toString().length()));
        } else {
            System.out.println(message);
        }
    }

    private void disconnect(String id, Globals.Status status) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId().equals(id)) {
                switch (status) {
                    case TIMEOUT:
                        System.out.println("[" + clients.get(i).getName() + "] times out");
                        break;
                    case LEFT:
                        System.out.println("[" + clients.get(i).getName() + "] left");
                        break;
                    case KICKED:
                        System.out.println("[" + clients.get(i).getName() + "] kicked");
                        break;
                }
                send(Globals.Prefix.KICKED.toString().getBytes(), clients.get(i).getAddress(), clients.get(i).getPort());
                clients.remove(clients.get(i));
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

    private void help() {
        System.out.println("[" + Globals.Command.TOGGLE_RAW_MODE + "] - toggle raw mode.");
        System.out.println("[" + Globals.Command.QUIT + "] - shut down the server.");
        System.out.println("[" + Globals.Command.HELP + "] - get help.");
        System.out.println("[" + Globals.Command.SEND_MESSAGE_TO_ALL + "] - send message to all clients.");
        System.out.println("[" + Globals.Command.KICK + "] - kick a user.");
        System.out.println("[" + Globals.Command.SHOW_CLIENTS + "] - show all connected clients.");
    }

    private void quit() {
        for (int i = 0; i < clients.size(); i++) {
            disconnect(clients.get(i).getId(), Globals.Status.KICKED);
        }
        running = false;
        socket.close();
        System.exit(-1);
    }
}
