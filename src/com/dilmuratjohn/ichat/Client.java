package com.dilmuratjohn.ichat;

import java.io.IOException;
import java.net.*;

class Client {

    private final int MAX_RECEIVE_BYTES = 1024;

    private DatagramSocket socket;
    private String name, address;
    private InetAddress ip;
    private int port;
    private String id;

    Client(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }

    String getName() {
        return name;
    }

    String getAddress() {
        return address;
    }

    InetAddress getIP() {
        return ip;
    }

    void setID(String id) {
        this.id = id;
    }

    boolean openConnection(String address) {
        try {
            socket = new DatagramSocket();
            ip = InetAddress.getByName(address);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    String receive() {
        byte[] data = new byte[MAX_RECEIVE_BYTES];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
            return "connection err.";
        }
        return new String(packet.getData(), packet.getOffset(), packet.getLength());
    }

    void send(final String data) {
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}