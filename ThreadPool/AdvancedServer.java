import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdvancedServer {
    private final ExecutorService threadPool;
    private ServerSocket serverSocket;

    public AdvancedServer(int port, int poolSize) throws IOException {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
        this.serverSocket = new ServerSocket(port);
        System.out.println(getTimestamp() + " [INFO] Server started on port " + port);
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println(getTimestamp() + " [INFO] Shutting down server...");
                threadPool.shutdown();
                serverSocket.close();
            } catch (IOException e) {
                System.err.println(getTimestamp() + " [ERROR] Error while shutting down: " + e.getMessage());
            }
        }));

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> handleClient(clientSocket));
            } catch (IOException e) {
                System.err.println(getTimestamp() + " [ERROR] Accept failed: " + e.getMessage());
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        String clientAddress = clientSocket.getRemoteSocketAddress().toString();
        System.out.println(getTimestamp() + " [INFO] Connected to " + clientAddress);

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            out.println("Welcome to the advanced echo server!");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(getTimestamp() + " [REQUEST from " + clientAddress + "] " + line);
                out.println("Echo: " + line);
            }
        } catch (IOException e) {
            System.err.println(getTimestamp() + " [ERROR] Client " + clientAddress + " error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println(getTimestamp() + " [INFO] Disconnected " + clientAddress);
            } catch (IOException e) {
                System.err.println(getTimestamp() + " [ERROR] Could not close connection: " + e.getMessage());
            }
        }
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static void main(String[] args) {
        int port = 8010;
        int poolSize = 10;
        try {
            new AdvancedServer(port, poolSize).start();
        } catch (IOException e) {
            System.err.println("Server could not start: " + e.getMessage());
        }
    }
}
