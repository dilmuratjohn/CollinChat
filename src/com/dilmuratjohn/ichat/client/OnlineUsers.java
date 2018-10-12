package com.dilmuratjohn.ichat.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OnlineUsers extends JFrame {
    private static final long serialVersionUID = 1L;

    private JList list;

    OnlineUsers() {
        setType(Type.UTILITY);
        setTitle("Online Users");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(200, 320);
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);
        setContentPane(panel);

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{0, 0};
        layout.rowHeights = new int[]{0, 0};
        layout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panel.setLayout(layout);

        list = new JList();
        GridBagConstraints GBCList = new GridBagConstraints();
        GBCList.fill = GridBagConstraints.BOTH;
        GBCList.gridx = 0;
        GBCList.gridy = 0;
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(list);
        panel.add(pane, GBCList);
        list.setFont(new Font("Serif", Font.ITALIC, 17));
    }

    void update(String[] users) {
        this.list.setListData(users);
    }
}
