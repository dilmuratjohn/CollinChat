package com.dilmuratjohn.ichat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel mPanel;
    private JTextField mTxtName;
    private JTextField mTxtAddress;
    private JTextField mTxtPort;
    private JLabel mLblName;
    private JLabel mLblAddress;
    private JLabel mLblPort;
    private JButton mBtnLogin;

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
        createWindow();
    }

    private void createWindow() {

        mPanel = new JPanel();
        mPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mPanel.setLayout(null);

        mTxtName = new JTextField();
        mTxtName.setBounds(125, 50, 150, 30);
        mTxtName.setColumns(10);

        mTxtAddress = new JTextField();
        mTxtAddress.setBounds(125, 100, 150, 30);
        mTxtAddress.setColumns(10);

        mTxtPort = new JTextField();
        mTxtPort.setBounds(125, 150, 150, 30);
        mTxtPort.setColumns(10);

        mLblName = new JLabel("Name:");
        mLblName.setBounds(30, 50, 70, 30);

        mLblAddress = new JLabel("IP Address:");
        mLblAddress.setBounds(30, 100, 70, 30);

        mLblPort = new JLabel("Port:");
        mLblPort.setBounds(30, 150, 70, 30);

        mBtnLogin = new JButton("Login");
        mBtnLogin.setBounds(100, 300, 100, 30);
        mBtnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = mTxtName.getText();
                String address = mTxtAddress.getText();
                int port = Integer.parseInt(mTxtPort.getText());
                login(name, address, port);
            }
        });

        mPanel.add(mTxtName);
        mPanel.add(mTxtAddress);
        mPanel.add(mTxtPort);
        mPanel.add(mLblName);
        mPanel.add(mLblAddress);
        mPanel.add(mLblPort);
        mPanel.add(mBtnLogin);

        setContentPane(mPanel);
    }

    private void login(String name, String address, int port) {
        if(name.equals("")) name = "anonymous user";
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
