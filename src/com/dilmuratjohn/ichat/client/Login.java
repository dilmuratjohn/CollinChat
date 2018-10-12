package com.dilmuratjohn.ichat.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField JTName;
    private JTextField JTAddress;
    private JTextField JTPort;
    private JLabel JLWarning;

    public Login() {
        createPanel();
    }

    private void createPanel() {

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

        JTName = new JTextField();
        JTName.setBounds(125, 50, 150, 30);
        JTName.setColumns(10);

        JTAddress = new JTextField();
        JTAddress.setBounds(125, 100, 150, 30);
        JTAddress.setColumns(10);

        JTPort = new JTextField();
        JTPort.setBounds(125, 150, 150, 30);
        JTPort.setColumns(10);

        JLabel JLName = new JLabel("Name:");
        JLName.setBounds(30, 50, 70, 30);

        JLabel JLAddress = new JLabel("IP Address:");
        JLAddress.setBounds(30, 100, 70, 30);

        JLabel JLPort = new JLabel("Port:");
        JLPort.setBounds(30, 150, 70, 30);

        JLWarning = new JLabel();
        JLWarning.setBounds(100, 270, 100, 30);
        JLWarning.setForeground(Color.RED);

        JButton JBLogin = new JButton("Login");
        JBLogin.setBounds(100, 300, 100, 30);
        JBLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JTName.getText();
                String address = JTAddress.getText();
                String port = (JTPort.getText());
                login(name, address, port);
            }
        });

        panel.add(JTName);
        panel.add(JTAddress);
        panel.add(JTPort);
        panel.add(JLName);
        panel.add(JLAddress);
        panel.add(JLPort);
        panel.add(JLWarning);
        panel.add(JBLogin);

        setContentPane(panel);
    }

    private void login(String name, String address, String port) {
        if (name.equals("")) {
            name = "anonymous user";
        }
        if (address.equals("")) {
            JLWarning.setText(" empty address");
            return;
        }
        if (port.equals("")) {
            JLWarning.setText("   empty port");
            return;
        }

        try {
            int iPort = Integer.parseInt(port);
            new ClientView(name, address, iPort);
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
