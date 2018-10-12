package com.dilmuratjohn.ichat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

class ClientView extends JFrame {

    private Client client;

    private static final long serialVersionUID = 1L;

    private JTextArea history;
    private JTextArea message;

    private boolean running = false;

    ClientView(final String name, final String address, final int port) {

        client = new Client(name, address, port);
        createView();

        boolean connect = client.openConnection(address);
        if (!connect) {
            running = false;
            System.err.println("Connection failed." + address + ":" + port);
            console("Connection failed.");
        } else {
            running = true;
            System.out.println("Connection succeed." + address + ":" + port);
            console("Attempting a connection to " + address + ":" + port + ", user: " + name + "...");
            String connection = Prefix.CONNECTION + name;
            client.send(connection);
            receive();
        }

    }

    private void createView() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("iChat");

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{55, 670, 15, 25, 15};
        layout.rowHeights = new int[]{30, 590, 15, 30, 15};
        layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{1.0, Double.MIN_VALUE};

        history = new JTextArea();
        DefaultCaret caret = (DefaultCaret) history.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane paneHistory = new JScrollPane(history);
        GridBagConstraints gbc_history = new GridBagConstraints();
        gbc_history.fill = GridBagConstraints.BOTH;
        gbc_history.gridx = 1;
        gbc_history.gridy = 1;
        gbc_history.gridwidth = 3;

        message = new JTextArea();
        JScrollPane paneMessage = new JScrollPane(message, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gbc_message = new GridBagConstraints();
        gbc_message.insets = new Insets(5, 0, 5, 0);
        gbc_message.fill = GridBagConstraints.BOTH;
        gbc_message.gridx = 1;
        gbc_message.gridy = 3;

        JButton send = new JButton("Send");
        GridBagConstraints gbc_send = new GridBagConstraints();
        gbc_send.fill = GridBagConstraints.HORIZONTAL;
        gbc_send.gridx = 3;
        gbc_send.gridy = 3;

        panel.setLayout(layout);
        panel.add(paneHistory, gbc_history);
        panel.add(paneMessage, gbc_message);
        panel.add(send, gbc_send);

        setContentPane(panel);
        setVisible(true);

        history.setEditable(false);
        history.setFont(new Font("Serif", Font.ITALIC, 17));
        history.setLineWrap(true);
        history.setWrapStyleWord(true);

        message.requestFocusInWindow();
        message.setFont(new Font("Serif", Font.ITALIC, 17));
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send(message.getText());
                }
            }
        });

        send.addActionListener(e -> {
            send(message.getText());
        });
    }

    private void send(String data) {
        data = data.trim();
        if (data.equals("")) return;

        data = Prefix.MESSAGE +
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) +
                "   " +
                client.getName() +
                ": \n" +
                data;
        client.send(data);
        message.setText("");
    }

    private void receive() {
        new Thread(() -> {
            while (running) {
                String data = client.receive();
                if (data.startsWith(Prefix.CONNECTION.toString())) {
                    client.setID(data.substring(Prefix.CONNECTION.toString().length()));
                    console("connection succeed.\n");
                } else {
                    System.out.println(data);
                    console(data.substring(Prefix.MESSAGE.toString().length()));
                }
            }
        }).start();
    }

    private void console(String message) {
        history.append(message + "\n");
        history.setCaretPosition(history.getDocument().getLength());
    }
}

