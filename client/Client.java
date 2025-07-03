import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    static Socket socket;
    static BufferedReader reader;
    static PrintWriter writer;

    public static void main(String[] args) {
        try {
            // Connect to server
            socket = new Socket("localhost", 1234);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // ===== GUI Login Dialog =====
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            Object[] fields = {
                "Enter username:", usernameField,
                "Enter password:", passwordField
            };

            int option = JOptionPane.showConfirmDialog(null, fields, "Login", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                writer.println(username);
                writer.println(password);
            } else {
                System.exit(0); // Exit if cancel
            }

            // ===== GUI Chat Window =====
            JFrame frame = new JFrame("Java Chat Client");
            JTextArea chatArea = new JTextArea();
            JTextField inputField = new JTextField();
            JButton sendButton = new JButton("Send");

            chatArea.setEditable(false);
            frame.setSize(500, 400);
            frame.setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.add(inputField, BorderLayout.CENTER);
            inputPanel.add(sendButton, BorderLayout.EAST);

            frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
            frame.add(inputPanel, BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            // ===== Handle Send Button and Enter Key =====
            ActionListener sendAction = e -> {
                String msg = inputField.getText().trim();
                if (!msg.isEmpty()) {
                    writer.println(msg); // send message to server
                    inputField.setText("");
                }
            };

            sendButton.addActionListener(sendAction);
            inputField.addActionListener(sendAction);

            // ===== Receive messages from server =====
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        // Show user list or chat message
                        if (msg.startsWith("🟢 Online Users:")) {
                            chatArea.append("\n" + msg + "\n");
                        } else {
                            chatArea.append(msg + "\n");
                        }
                    }
                } catch (IOException e) {
                    chatArea.append("Disconnected from server.\n");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "❌ Unable to connect to server.");
        }
    }
}
