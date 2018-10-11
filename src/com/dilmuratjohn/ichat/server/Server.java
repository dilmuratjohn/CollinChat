package com.dilmuratjohn.ichat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server implements Runnable {

    private int mPort;
    private boolean running = false;
    private DatagramSocket mSocket;

    private Thread mThreadRun, mThreadSend, mThreadReceive, mThreadManage;

    public Server(int port) {
        mPort = port;
        try {
            mSocket = new DatagramSocket(mPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        mThreadRun = new Thread(this, "server");
        mThreadRun.start();
    }

    public void run() {
        running = true;
        System.out.println("Server listening on port " + mPort + "...");
        manageClients();
        receive();
    }

    private void manageClients() {
        mThreadManage = new Thread("manage") {
            public void run() {
                while (running) {
                    // TODO managing
                }
            }
        };
        mThreadManage.start();
    }

    private void receive() {
        mThreadReceive = new Thread("receive") {
            public void run() {
                while (running) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        mSocket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    System.out.println(message);
                }
            }
        };
        mThreadReceive.start();
    }
}
