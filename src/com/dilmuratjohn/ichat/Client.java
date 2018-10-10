package com.dilmuratjohn.ichat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class Client extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel panel;
    private String name;
    private String address;
    private int port;

    Client(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;

        this.createWindow();
        this.createPanel();
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
        setVisible(true);
        setTitle("Collin Chat Client");
        requestFocus();
    }

    private void createPanel() {
        this.panel = new JPanel();
        this.panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{55, 670, 15, 25, 15};
        layout.rowHeights = new int[]{30, 595, 15, 25, 15};
        layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        this.panel.setLayout(layout);

        JTextArea txtHistory = new JTextArea();
        txtHistory.setEditable(false);
        GridBagConstraints gbc_txtHistory = new GridBagConstraints();
        gbc_txtHistory.fill = GridBagConstraints.BOTH;
        gbc_txtHistory.gridx = 1;
        gbc_txtHistory.gridy = 1;
        gbc_txtHistory.gridwidth = 3;
        this.panel.add(txtHistory, gbc_txtHistory);

        JTextArea txtMessage = new JTextArea("say some thing.");
        txtMessage.requestFocus();
        GridBagConstraints gbc_txtMessage = new GridBagConstraints();
        gbc_txtMessage.fill = GridBagConstraints.BOTH;
        gbc_txtMessage.gridx = 1;
        gbc_txtMessage.gridy = 3;
        this.panel.add(txtMessage, gbc_txtMessage);

        JButton btnSend = new JButton("Send");
        GridBagConstraints gbc_btnSend = new GridBagConstraints();
        gbc_btnSend.fill = GridBagConstraints.BOTH;
        gbc_btnSend.gridx = 3;
        gbc_btnSend.gridy = 3;
        this.panel.add(btnSend, gbc_btnSend);

        setContentPane(this.panel);
    }
}
