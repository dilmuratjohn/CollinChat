package com.dilmuratjohn.ichat.server;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Server implements Runnable {

    private int mPort;
    private boolean running = false;
    private DatagramSocket mSocket;

    private Thread run, send, receive, manage;

    public Server(int port) {
        mPort = port;
        try {
            mSocket = new DatagramSocket(mPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        run = new Thread(this, "server");
    }

    public void run() {
        running = true;
        manageClients();
        receive();
    }

    private void manageClients() {
        manage = new Thread("manage"){
            public void run(){
              while (running){
                  // TODO managing
              }
            }
        };
        manage.run();
    }

    private void receive() {
        receive = new Thread("receive"){
            public void run(){
                while (running){
                    // TODO receiving
                }
            }
        };
        receive.run();
    }
}
