package ru.nsu.ccfit.skokova.chat.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.Client;
import ru.nsu.ccfit.skokova.chat.Server;
import ru.nsu.ccfit.skokova.chat.message.LogoutMessage;
import ru.nsu.ccfit.skokova.chat.message.UserListMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends JFrame {
    private Client client;

    private static final Logger logger = LogManager.getLogger(ClientFrame.class);

    private JTextField serverField;
    private JTextField portField;
    private JTextField usernameField;
    private JTextArea messageArea;

    private JButton loginButton;
    private JButton logoutButton;
    private JButton usersButton;

    private JPanel connectionPanel;

    public ClientFrame(Client client) {
        super("Chat Client");
        this.client = client;
        createAndShowGUI();
        setButtons();
        setConnectionInfo();
    }

    private void setConnectionInfo() {
        this.serverField.addActionListener(new ServerListener());
        this.portField.addActionListener(new PortListener());
        this.usernameField.addActionListener(new UsernameListener());
    }

    private void setButtons() {
        this.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                client.start();
            }
        });

        this.logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                client.sendMessage(new LogoutMessage());
            }
        });

        this.usersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                client.sendMessage(new UserListMessage());
            }
        });
    }

    private void createAndShowGUI() {
        addConnectionInfo();
        //this.connectionPanel.setBackground(new Color(240, 255, 255));
        addLoginInfo();
        addChatPanel();
        addButtonPanel();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
    }

    private void addConnectionInfo() {
        this.connectionPanel = new JPanel(new GridLayout(3,1));
        JPanel serverAndPortPanel = new JPanel(new GridLayout(1, 5, 1, 3));
        this.serverField = new JTextField("Server address");
        this.portField = new JTextField("Port");
        this.serverField.setHorizontalAlignment(SwingConstants.LEFT);
        this.portField.setHorizontalAlignment(SwingConstants.RIGHT);
        serverAndPortPanel.add(this.serverField);
        serverAndPortPanel.add(this.portField);
        this.connectionPanel.add(serverAndPortPanel);
        this.add(this.connectionPanel, BorderLayout.NORTH);
    }

    private void addLoginInfo() {
        JLabel usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        this.usernameField = new JTextField("Username");
        this.connectionPanel.add(usernameLabel);
        this.connectionPanel.add(this.usernameField);
    }

    private void addChatPanel() {
        this.messageArea = new JTextArea("Welcome to the Chat");
        JScrollPane messageScrollPane = new JScrollPane(this.messageArea);
        JPanel chatPanel = new JPanel(new GridLayout(1, 1));
        chatPanel.add(messageScrollPane);
        this.messageArea.setEditable(false);
        this.add(chatPanel, BorderLayout.CENTER);
    }

    private void addButtonPanel() {
        this.loginButton = new JButton("Login");
        this.loginButton.setBackground(new Color(32, 50, 100));
        this.loginButton.setForeground(Color.WHITE);
        this.logoutButton = new JButton("Logout");
        this.logoutButton.setForeground(Color.WHITE);
        this.logoutButton.setBackground(new Color(32, 50, 100));
        this.usersButton = new JButton("UserList");
        this.usersButton.setForeground(Color.WHITE);
        this.usersButton.setBackground(new Color(32, 50, 100));

        JPanel buttonPanel = new JPanel();
        //buttonPanel.setBackground(new Color(240, 255, 255));
        buttonPanel.add(this.loginButton);
        buttonPanel.add(this.usersButton);
        buttonPanel.add(this.logoutButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    class PortListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                int value = Integer.parseInt(portField.getText());
                if ((value < Server.MIN_PORT_NUMBER) || (value > Server.MAX_PORT_NUMBER)) {
                    throw new NumberFormatException("Incorrect value");
                }
                client.setPort(value);
            } catch (NumberFormatException e) {
                portField.setForeground(Color.RED);
                logger.error("Wrong field value");
            }
        }
    }

    class ServerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                String value = serverField.getText();
                client.setServer(value);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    class UsernameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                String value = usernameField.getText();
                client.setUsername(value);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
