import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    private static final Map<String, ClientHandler> clients = Collections.synchronizedMap(new HashMap<>());
    private static final File logFile = new File(System.getProperty("user.home") +
            "/chatlog-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt");

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            username = reader.readLine();
            String password = reader.readLine();

            synchronized (clients) {
                if (clients.containsKey(username)) {
                    writer.println("❌ Username already taken. Please try again with a different name.");
                    socket.close();
                    return;
                }
                clients.put(username, this);
            }

            broadcast("✅ " + username + " joined the chat.");
            sendUserListToAll();

            String message;
            while ((message = reader.readLine()) != null) {
                String timestamp = "[" + new SimpleDateFormat("HH:mm").format(new Date()) + "]";
                if (message.startsWith("@")) {
                    int space = message.indexOf(" ");
                    if (space != -1) {
                        String targetUser = message.substring(1, space);
                        String privateMsg = message.substring(space + 1);
                        sendPrivate(targetUser, timestamp + " " + username + " (PM): " + privateMsg);
                        log(timestamp + " [PM] " + username + " -> " + targetUser + ": " + privateMsg);
                    }
                } else {
                    broadcast(timestamp + " " + username + ": " + message);
                    log(timestamp + " " + username + ": " + message);
                }
            }
        } catch (IOException e) {
            System.out.println(username + " disconnected.");
        } finally {
            try {
                socket.close();
                clients.remove(username);
                broadcast("❌ " + username + " left the chat.");
                sendUserListToAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPrivate(String targetUser, String message) {
        ClientHandler target = clients.get(targetUser);
        if (target != null) {
            target.writer.println(message);
            if (!targetUser.equals(this.username)) {
                this.writer.println("🟢 You to @" + targetUser + ": " + message);
            }
        } else {
            writer.println("⚠️ User '" + targetUser + "' not found.");
        }
    }

    private void broadcast(String message) {
        for (ClientHandler client : clients.values()) {
            client.writer.println(message);
        }
    }

    private void sendUserListToAll() {
        StringBuilder userList = new StringBuilder("🟢 Online Users:\n");
        for (String name : clients.keySet()) {
            userList.append("• ").append(name).append("\n");
        }
        for (ClientHandler client : clients.values()) {
            client.writer.println(userList.toString());
        }
    }

    private void log(String line) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(line + "\n");
            System.out.println("💾 Log written: " + line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
