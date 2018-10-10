package com.dilmuratjohn.ichat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField textName;
    private JTextField textAddress;
    private JTextField textPort;
    private JLabel labelName;
    private JLabel labelAddress;
    private JLabel labelPort;
    private JButton buttonLogin;

    public Login() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Login");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(null);
        setContentPane(panel);

        this.addParts(panel);

    }

    private void addParts(JPanel panel) {

        this.textName = new JTextField();
        this.textName.setBounds(125, 50, 150, 30);
        this.textName.setColumns(10);

        this.textAddress = new JTextField();
        this.textAddress.setBounds(125, 100, 150, 30);
        this.textAddress.setColumns(10);

        this.textPort = new JTextField();
        this.textPort.setBounds(125, 150, 150, 30);
        this.textPort.setColumns(10);

        this.labelName = new JLabel("Name:");
        this.labelName.setBounds(30, 50, 70, 30);

        this.labelAddress = new JLabel("IP Address:");
        this.labelAddress.setBounds(30, 100, 70, 30);

        this.labelPort = new JLabel("Port:");
        this.labelPort.setBounds(30, 150, 70, 30);

        this.buttonLogin = new JButton();
        this.buttonLogin.setBounds(100, 300, 100, 30);
        this.buttonLogin.setText("Login");

        panel.add(this.textName);
        panel.add(this.textAddress);
        panel.add(this.textPort);
        panel.add(this.labelName);
        panel.add(this.labelAddress);
        panel.add(this.labelPort);
        panel.add(this.buttonLogin);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
