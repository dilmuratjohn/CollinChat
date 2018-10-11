package com.dilmuratjohn.ichat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Client extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel panel;
    private JTextArea txtHistory;
    private JTextArea txtMessage;
    private JButton btnSend;
    private String name;
    private String address;
    private int port;

    Client(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;

        this.createWindow();
        this.console("Attempting a connection to " + address + ":" + port + ", user: " + name + "...");
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
        setTitle("Collin Chat Client");

        this.panel = new JPanel();
        this.panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{55, 670, 15, 25, 15};
        layout.rowHeights = new int[]{30, 595, 15, 25, 15};
        layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        this.panel.setLayout(layout);

        this.txtHistory = new JTextArea();
        GridBagConstraints gbc_txtHistory = new GridBagConstraints();
        gbc_txtHistory.fill = GridBagConstraints.BOTH;
        gbc_txtHistory.gridx = 1;
        gbc_txtHistory.gridy = 1;
        gbc_txtHistory.gridwidth = 3;
        this.panel.add(this.txtHistory, gbc_txtHistory);

        this.txtMessage = new JTextArea();
        GridBagConstraints gbc_txtMessage = new GridBagConstraints();
        gbc_txtMessage.fill = GridBagConstraints.BOTH;
        gbc_txtMessage.gridx = 1;
        gbc_txtMessage.gridy = 3;
        gbc_txtMessage.insets = new Insets(5, 0, 5, 0);
        this.panel.add(txtMessage, gbc_txtMessage);

        this.btnSend = new JButton("Send");
        GridBagConstraints gbc_btnSend = new GridBagConstraints();
        gbc_btnSend.fill = GridBagConstraints.BOTH;
        gbc_btnSend.gridx = 3;
        gbc_btnSend.gridy = 3;
        this.panel.add(btnSend, gbc_btnSend);

        setContentPane(this.panel);
        setVisible(true);

        txtHistory.setEditable(false);
        txtMessage.requestFocusInWindow();
        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send(txtMessage.getText());
                }
            }
        });
        btnSend.addActionListener(e -> {
            send(txtMessage.getText());
        });

        txtMessage.requestFocusInWindow();
    }

    private void send(String message) {
        if (message.equals("")) return;
        this.txtMessage.setText("");
        console(name + ": " + message);
    }

    private void console(String message) {
        this.txtHistory.append(message + "\n");
    }
}
