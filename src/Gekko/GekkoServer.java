package Gekko;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class GekkoServer {
    ServerSocket socket;
    int port = 8080;
    int backlog = 10;

    public GekkoServer() throws IOException {
        socket = new ServerSocket(port, backlog);
    }
    public GekkoServer(int port, int backlog) throws IOException {
        this.port = port;
        this.backlog = backlog;
        socket = new ServerSocket(port, backlog);
    }
    public void runServer() throws IOException {
        System.out.println("Waiting for connections...");
        Socket client = socket.accept();
        sendResponse(client);
        client.close();
        socket.close();
    }

    private void sendResponse(Socket client) throws IOException {
        String response = """
                HTTP/1.0 200 OK
                Server: Gekko
                Content-Type: text/html
                Content-Length: 22
                
                <h1>Hello, Gekko!</h1>
                """;
        byte[] responseBytes = response.getBytes();
        OutputStream outputStream = client.getOutputStream();
        outputStream.write(responseBytes);
        outputStream.close();
    }
}
