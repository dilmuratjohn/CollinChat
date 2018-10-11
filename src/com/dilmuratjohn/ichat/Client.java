package com.dilmuratjohn.ichat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Client extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel mPanel;
    private JTextArea mTxtHistory;
    private JTextArea mTxtMessage;
    private JButton mBtnSend;
    private DefaultCaret mCaret;
    private String mMame;
    private String mAddress;
    private int mPort;

    Client(String name, String address, int port) {

        mMame = name;
        mAddress = address;
        mPort = port;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Collin Chat Client");

        createWindow();
        console("Attempting a connection to " + mAddress + ":" + port + ", user: " + mMame + "...");
    }

    private void createWindow() {

        mPanel = new JPanel();
        mPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{55, 670, 15, 25, 15};
        layout.rowHeights = new int[]{30, 590, 15, 30, 15};
        layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{1.0, Double.MIN_VALUE};

        mTxtHistory = new JTextArea();
        mCaret = (DefaultCaret) mTxtHistory.getCaret();
        mCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollHistory = new JScrollPane(mTxtHistory);
        GridBagConstraints scrollConstraintsHistory = new GridBagConstraints();
        scrollConstraintsHistory.fill = GridBagConstraints.BOTH;
        scrollConstraintsHistory.gridx = 1;
        scrollConstraintsHistory.gridy = 1;
        scrollConstraintsHistory.gridwidth = 3;

        mTxtMessage = new JTextArea();
        JScrollPane scrollMessage = new JScrollPane(mTxtMessage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints scrollConstraintsMessage = new GridBagConstraints();
        scrollConstraintsMessage.insets = new Insets(5, 0, 5, 0);
        scrollConstraintsMessage.fill = GridBagConstraints.BOTH;
        scrollConstraintsMessage.gridx = 1;
        scrollConstraintsMessage.gridy = 3;

        mBtnSend = new JButton("Send");
        GridBagConstraints gbc_btnSend = new GridBagConstraints();
        gbc_btnSend.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSend.gridx = 3;
        gbc_btnSend.gridy = 3;

        mPanel.setLayout(layout);
        mPanel.add(scrollHistory, scrollConstraintsHistory);
        mPanel.add(scrollMessage, scrollConstraintsMessage);
        mPanel.add(mBtnSend, gbc_btnSend);

        setContentPane(mPanel);
        setVisible(true);

        mTxtHistory.setEditable(false);
        mTxtHistory.setFont(new Font("Serif", Font.ITALIC, 17));
        mTxtHistory.setLineWrap(true);
        mTxtHistory.setWrapStyleWord(true);

        mTxtMessage.requestFocusInWindow();
        mTxtMessage.setFont(new Font("Serif", Font.ITALIC, 17));
        mTxtMessage.setLineWrap(true);
        mTxtMessage.setWrapStyleWord(true);
        mTxtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send(mTxtMessage.getText());
                }
            }
        });

        mBtnSend.addActionListener(e -> {
            send(mTxtMessage.getText());
        });
    }

    private void send(String message) {
        message = message.trim();
        if (message.equals("")) return;
        console(mMame + ": " + message);
        mTxtMessage.setText("");
    }

    private void console(String message) {
        mTxtHistory.append(message + "\n");
        mTxtHistory.setCaretPosition(mTxtHistory.getDocument().getLength());
    }
}
