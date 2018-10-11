package com.dilmuratjohn.ichat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.*;

class Client extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextArea mTxtHistory;
    private JTextArea mTxtMessage;

    private String mMame;
    private String mAddress;
    private InetAddress mIP;
    private int mPort;
    private DatagramSocket mSocket;
    private boolean running = false;

    Client(String name, String address, int port) {

        mMame = name;
        mAddress = address;
        mPort = port;

        createWindow();

        boolean connect = openConnection(mAddress);
        if (!connect) {
            running = false;
            System.err.println("Connection failed." + mAddress + ":" + mPort);
            console("Connection failed.");
        } else {
            running = true;
            System.out.println("Connection succeed." + mAddress + ":" + mPort);
            console("Attempting a connection to " + mAddress + ":" + mPort + ", user: " + mMame + "...");
            String connection = "/c/" + mMame;
            send(connection.getBytes());
            receive();
        }

    }

    private boolean openConnection(String address) {
        try {
            mSocket = new DatagramSocket();
            mIP = InetAddress.getByName(address);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void receive() {
        Thread receive = new Thread("Client") {
            public void run() {
                while (running){
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        mSocket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    console(message.substring(3));
                }
            }
        };
        receive.start();
    }

    private void send(String message) {
        message = message.trim();
        if (message.equals("")) return;
        String string = mMame + ": " + message;
        console(string);
        string = "/m/" + string;
        send(string.getBytes());
        mTxtMessage.setText("");
    }

    private void send(final byte[] data) {
        Thread mThreadSend = new Thread("send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, mIP, mPort);
                try {
                    mSocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThreadSend.start();

    }

    private void console(String message) {
        mTxtHistory.append(message + "\n");
        mTxtHistory.setCaretPosition(mTxtHistory.getDocument().getLength());
    }

    private void createWindow() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Collin Chat Client");

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{55, 670, 15, 25, 15};
        layout.rowHeights = new int[]{30, 590, 15, 30, 15};
        layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{1.0, Double.MIN_VALUE};

        mTxtHistory = new JTextArea();
        DefaultCaret mCaret = (DefaultCaret) mTxtHistory.getCaret();
        mCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollHistory = new JScrollPane(mTxtHistory);
        GridBagConstraints scrollConstraintsHistory = new GridBagConstraints();
        scrollConstraintsHistory.fill = GridBagConstraints.BOTH;
        scrollConstraintsHistory.gridx = 1;
        scrollConstraintsHistory.gridy = 1;
        scrollConstraintsHistory.gridwidth = 3;

        mTxtMessage = new JTextArea();
        JScrollPane scrollMessage = new JScrollPane(mTxtMessage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints scrollConstraintsMessage = new GridBagConstraints();
        scrollConstraintsMessage.insets = new Insets(5, 0, 5, 0);
        scrollConstraintsMessage.fill = GridBagConstraints.BOTH;
        scrollConstraintsMessage.gridx = 1;
        scrollConstraintsMessage.gridy = 3;

        JButton btnSend = new JButton("Send");
        GridBagConstraints gbc_btnSend = new GridBagConstraints();
        gbc_btnSend.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSend.gridx = 3;
        gbc_btnSend.gridy = 3;

        panel.setLayout(layout);
        panel.add(scrollHistory, scrollConstraintsHistory);
        panel.add(scrollMessage, scrollConstraintsMessage);
        panel.add(btnSend, gbc_btnSend);

        setContentPane(panel);
        setVisible(true);

        mTxtHistory.setEditable(false);
        mTxtHistory.setFont(new Font("Serif", Font.ITALIC, 17));
        mTxtHistory.setLineWrap(true);
        mTxtHistory.setWrapStyleWord(true);

        mTxtMessage.requestFocusInWindow();
        mTxtMessage.setFont(new Font("Serif", Font.ITALIC, 17));
        mTxtMessage.setLineWrap(true);
        mTxtMessage.setWrapStyleWord(true);
        mTxtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send(mTxtMessage.getText());
                }
            }
        });

        btnSend.addActionListener(e -> {
            send(mTxtMessage.getText());
        });
    }
}
