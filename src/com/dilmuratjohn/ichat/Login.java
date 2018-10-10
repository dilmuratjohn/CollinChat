package com.dilmuratjohn.ichat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel panel;
    private JTextField txtName;
    private JTextField txtAddress;
    private JTextField txtPort;
    private JLabel lblName;
    private JLabel lblAddress;
    private JLabel lblPort;
    private JButton btnLogin;

    public Login() {
        this.createWindow();
    }

    private void createWindow() {
        this.setUp();
        this.createPanel();
        this.addParts();
    }

    private void setUp() {
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
    }

    private void createPanel() {
        this.panel = new JPanel();
        this.panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.panel.setLayout(null);
        setContentPane(this.panel);
    }

    private void addParts() {
        // name text area
        this.txtName = new JTextField();
        this.txtName.setBounds(125, 50, 150, 30);
        this.txtName.setColumns(10);
        // address text area
        this.txtAddress = new JTextField();
        this.txtAddress.setBounds(125, 100, 150, 30);
        this.txtAddress.setColumns(10);
        // port text area
        this.txtPort = new JTextField();
        this.txtPort.setBounds(125, 150, 150, 30);
        this.txtPort.setColumns(10);
        // name label
        this.lblName = new JLabel("Name:");
        this.lblName.setBounds(30, 50, 70, 30);
        // address label
        this.lblAddress = new JLabel("IP Address:");
        this.lblAddress.setBounds(30, 100, 70, 30);
        // port label
        this.lblPort = new JLabel("Port:");
        this.lblPort.setBounds(30, 150, 70, 30);
        // login button
        this.btnLogin = new JButton();
        this.btnLogin.setBounds(100, 300, 100, 30);
        this.btnLogin.setText("Login");
        this.btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("[Login] -- login button clicked.");
                String name = txtName.getText();
                String address = txtAddress.getText();
                int port = Integer.parseInt(txtPort.getText());
                login(name, address, port);
            }
        });

        panel.add(this.txtName);
        panel.add(this.txtAddress);
        panel.add(this.txtPort);
        panel.add(this.lblName);
        panel.add(this.lblAddress);
        panel.add(this.lblPort);
        panel.add(this.btnLogin);

    }

    private void login(String name, String address, int port) {
        dispose();
        new Client(name, address, port);
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
