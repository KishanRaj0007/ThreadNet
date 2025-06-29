import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
    
    public void run() throws IOException, UnknownHostException{
        int port = 8010;
        ServerSocket socket = new ServerSocket(port); //  listen for incoming TCP connections.
        socket.setSoTimeout(20000); //  If no client connects in this time, accept() will throw a SocketTimeoutException
        while(true){ // server will keep running and accept one client at a time.
            System.out.println("Server is listening on port: "+port);
            Socket acceptedConnection = socket.accept(); //  accept() blocks until a client connects.
            //  When a client connects, a new Socket is created for the connection.
            //  The server can then communicate with the client using this Socket. 
            System.out.println("Connected to "+acceptedConnection.getRemoteSocketAddress()); // Print the address of the connected client.
            PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream(), true); //  Create a PrintWriter to send text data to the client.
            //  The second argument 'true' enables auto-flushing, so that the output is sent immediately without needing to call flush() explicitly.
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream())); // Create a BufferedReader to read text data from the client.
            toClient.println("Hello World from the server");
            
        }
    }

    public static void main(String[] args){
        Server server = new Server();
        try{
            server.run(); // Start the server.
        }catch(Exception ex){
            ex.printStackTrace(); 
        }
    }

}
