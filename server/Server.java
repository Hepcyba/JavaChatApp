import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static Map<String, ClientHandler> clientHandlers = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        final int PORT = 1234;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("✅ Server started. Waiting for clients on port " + PORT + "...");

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, clientHandlers);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("❌ Server Error: " + e.getMessage());
        }
    }
}
