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

    Client(String name,String address,int port) {
        this.name = name;
        this.address = address;
        this.port = port;

        this.createWindow();
    }

    private void createWindow() {
        this.setUp();
        this.createPanel();
        this.addPanelParts();
    }

    private void setUp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("Collin Chat Client");
    }

    private void createPanel() {
        this.panel = new JPanel();
        this.panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.panel.setLayout(new BorderLayout(0, 0));
        setContentPane(this.panel);
    }

    private void addPanelParts() {
    }
}
