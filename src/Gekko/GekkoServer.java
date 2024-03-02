package Gekko;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

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
        System.out.printf("Listening on port %d\n", this.port);
        System.out.println("Waiting for connections...");
        Socket client = socket.accept();
        Request request = getRequest(client);
        BaseResponse response = new BaseResponse();
        response.setJSONBody("{message: \"Hello, Gekkos\"}");
        sendResponse(client, response);
        client.close();
        socket.close();
    }

    public Request getRequest(Socket client) throws IOException {
        InputStream inputStream = client.getInputStream();
        ArrayList<String> requestLines = new ArrayList<>();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        while (reader.ready()) {
            String line = reader.readLine();
            if (line.isEmpty()) continue;
            requestLines.add(line);
        }

        return new Request(requestLines);
    }

    private void sendResponse(Socket client, BaseResponse response) throws IOException {
        byte[] responseBytes = response.sendable();
        OutputStream outputStream = client.getOutputStream();
        outputStream.write(responseBytes);
    }
}
