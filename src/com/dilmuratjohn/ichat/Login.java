package com.dilmuratjohn.ichat;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField textName;
    private JLabel labelName;
    private JTextField textPassword;
    private JLabel labelPassword;

    public Login() {

        setTitle("Login");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 500);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        this.addParts();


    }

    private void addParts() {

        textName = new JTextField();
        textName.setBounds(125, 50, 150, 30);
        textName.setColumns(10);

        textPassword = new JTextField();
        textPassword.setBounds(125, 100, 150, 30);
        textPassword.setColumns(10);

        labelName = new JLabel("Name:");
        labelName.setBounds(30, 50, 70, 30);

        labelPassword = new JLabel("Password:");
        labelPassword.setBounds(30, 100, 70, 30);

        this.contentPane.add(textName);
        this.contentPane.add(textPassword);
        this.contentPane.add(labelName);
        this.contentPane.add(labelPassword);

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
