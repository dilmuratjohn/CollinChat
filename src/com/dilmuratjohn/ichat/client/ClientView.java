package com.dilmuratjohn.ichat.client;

import com.dilmuratjohn.ichat.Globals;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

class ClientView extends JFrame {

    private static final long serialVersionUID = 1L;
    private boolean running;
    private Client client;
    private JTextArea JTMessage;
    private JTextArea JTHistory;
    private OnlineUsers onlineUsers;

    private final String address;

    ClientView(final String name, final String address, final int port) {
        this.address = address;
        createView();
        this.client = new Client(name, port);
        init();
    }

    private void init() {
        boolean connect = client.openConnection(this.address);
        if (!connect) {
            running = false;
            System.err.println("Connection failed." + address + ":" + client.getPort());
            console("Connection failed.");
        } else {
            running = true;
            System.out.println("Connection succeed." + address + ":" + client.getPort());
            console("Attempting a connection to " + address + ":" + client.getPort() + ", user: " + client.getName() + "...");
            String connection = Globals.Prefix.CONNECTION + client.getName();
            client.send(connection);
            receive();
            this.onlineUsers = new OnlineUsers();
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

        JMenuBar JMBar = new JMenuBar();
        setJMenuBar(JMBar);

        JMenu JMFile = new JMenu("File");
        JMBar.add(JMFile);

        JMenuItem JMIOnlineUser = new JMenuItem("Online User");
        JMenuItem JMIExit = new JMenuItem("Exit");
        JMFile.add(JMIOnlineUser);
        JMFile.add(JMIExit);

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{55, 670, 15, 25, 15};
        layout.rowHeights = new int[]{50, 570, 15, 30, 15};
        layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{1.0, Double.MIN_VALUE};

        JTHistory = new JTextArea();
        JTHistory.setEditable(false);
        JTHistory.setFont(new Font("Serif", Font.ITALIC, 17));
        JTHistory.setLineWrap(true);
        JTHistory.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret) JTHistory.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane JSPHistory = new JScrollPane(JTHistory);
        GridBagConstraints GBCHistory = new GridBagConstraints();
        GBCHistory.fill = GridBagConstraints.BOTH;
        GBCHistory.gridx = 1;
        GBCHistory.gridy = 1;
        GBCHistory.gridwidth = 3;

        JTMessage = new JTextArea();
        JTMessage.setFont(new Font("Serif", Font.ITALIC, 17));
        JTMessage.setLineWrap(true);
        JTMessage.setWrapStyleWord(true);
        JScrollPane JSPMessage = new JScrollPane(JTMessage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints GBCMessage = new GridBagConstraints();
        GBCMessage.insets = new Insets(5, 0, 5, 0);
        GBCMessage.fill = GridBagConstraints.BOTH;
        GBCMessage.gridx = 1;
        GBCMessage.gridy = 3;

        JButton JBSend = new JButton("Send");
        GridBagConstraints GBCSend = new GridBagConstraints();
        GBCSend.fill = GridBagConstraints.HORIZONTAL;
        GBCSend.gridx = 3;
        GBCSend.gridy = 3;

        panel.setLayout(layout);
        panel.add(JSPHistory, GBCHistory);
        panel.add(JSPMessage, GBCMessage);
        panel.add(JBSend, GBCSend);

        setContentPane(panel);
        setVisible(true);

        JTMessage.requestFocusInWindow();

        JMIOnlineUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onlineUsers.setVisible(true);
            }
        });
        JMIExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JTMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send(JTMessage.getText());
                }
            }
        });
        JBSend.addActionListener(e -> {
            send(JTMessage.getText());
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (running) {
                    client.send(Globals.Prefix.DISCONNECTION + client.getId());
                    client.close();
                }
                running = false;
            }
        });
    }

    private void send(String message) {
        message = message.trim();
        if (message.equals("")) return;

        message = Globals.Prefix.MESSAGE +
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) +
                "   " +
                client.getName() +
                ": \n" +
                message;
        System.out.println(message);
        client.send(message);
        JTMessage.setText("");
    }

    private void receive() {
        new Thread(() -> {
            while (running) {
                String message = client.receive();
                if (message.startsWith(Globals.Prefix.CONNECTION.toString())) {
                    client.setId(message.substring(Globals.Prefix.CONNECTION.toString().length()));
                    console("Connection succeed.\n");
                } else if (message.startsWith(Globals.Prefix.PING.toString())) {
                    client.send(Globals.Prefix.PING.toString() + client.getId());
                } else if (message.startsWith(Globals.Prefix.KICKED.toString())) {
                    console("You are kicked by root.");
                    running = false;
                } else if (message.startsWith(Globals.Prefix.ONLINE_USER.toString())) {
                    String[] userList = message.split(Globals.Prefix.ONLINE_USER.toString());
                    this.onlineUsers.update(Arrays.copyOfRange(userList, 1, userList.length));
                } else {
                    System.out.println(message);
                    console(message.substring(Globals.Prefix.MESSAGE.toString().length()));
                }
            }
        }).start();
    }

    private void console(String message) {
        JTHistory.append(message + "\n");
        JTHistory.setCaretPosition(JTHistory.getDocument().getLength());
    }
}
