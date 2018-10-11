package com.dilmuratjohn.ichat;

import java.io.IOException;
import java.net.*;

class Client {

    private DatagramSocket socket;
    private String name, address;
    private InetAddress ip;
    private int port;
    private boolean running = false;

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

    void receive() {
        Thread receive = new Thread("Client") {
            public void run() {
                while (running) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                }
            }
        };
        receive.start();
    }

    void send(final byte[] data) {
        Thread mThreadSend = new Thread("send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThreadSend.start();
    }
}
