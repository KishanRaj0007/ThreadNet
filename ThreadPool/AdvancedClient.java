import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AdvancedClient {
    public static void main(String[] args) {
        int port = 8010;
        try (
            Socket socket = new Socket("localhost", port);
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("[CONNECTED] Server says: " + fromServer.readLine());

            String input;
            while (true) {
                System.out.print("Enter message (or type 'exit'): ");
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) break;
                toServer.println(input);
                System.out.println("Server replied: " + fromServer.readLine());
            }
        } catch (IOException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }
}
