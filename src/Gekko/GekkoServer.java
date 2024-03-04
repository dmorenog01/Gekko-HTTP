package Gekko;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class GekkoServer {
    ServerSocket socket;
    int port = 8080;
    int backlog = 10;
    boolean running = true;
    Hashtable<String, Hashtable<String, Handler>> handlers = new Hashtable<>();

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

        while (this.running) {
            // Services Request and closes connection to client
            Socket client = socket.accept();
            Request request;
            request = getRequest(client);
            serviceRequest(client, request);
            client.close();
        }
        socket.close();
    }

    public void addHandler(String path, String[] methods, Handler handler) {
        addHandler(path, methods, handler, false);
    }

    public void addHandler(String path, String[] methods, Handler handler, boolean show) {
        Hashtable<String, Handler> handlerTable = new Hashtable<>();
        for (String method : methods) {
            handlerTable.put(method, handler);
        }
        this.handlers.put(path, handlerTable);
        if (show) System.out.printf("Registered %s\n", path);
    }

    private void serviceRequest(Socket client, Request request) throws IOException {
        // Read request and pass it to appropriate request handler
        System.out.println("REQUEST:\n" + request);

        BaseResponse response = getResponse(request);
//        System.out.println("RESPONSE:\n" + response);

        sendResponse(client, response);
    }

    private BaseResponse getResponse(Request request) {
        Handler handler = null;
//        System.out.printf("Searching handler for %s %s\n", request.path, request.verb);
        try {
            Hashtable<String, Handler> pathHandler = this.handlers.get(request.path);
            if (pathHandler == null) return new ServerErrorResponse();

            handler = pathHandler.get(request.verb);
            if (handler == null) return new ServerErrorResponse();


        } catch (Exception e) {
            System.out.println("ERROR" + e.getMessage());
            System.out.println("RAW REQUEST" + request.rawRequest);
            System.exit(1);
        }
        BaseResponse emptyResponse = new BaseResponse();
//        System.out.printf("Handling %s %s\n", request.path, request.verb);
        return handler.getResponse(request, emptyResponse);
    }

    private Request getRequest(Socket client) throws IOException {
        InputStream inputStream = client.getInputStream();
        ArrayList<String> requestLines = new ArrayList<>();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        // Read head
        String line = reader.readLine();
        while (line != null && !line.isBlank()) {
            requestLines.add(line);
            line = reader.readLine();
        }
        Request request = new Request(requestLines);

        Header contentLengthHeader = request.headers.get("Content-Length");

        if (contentLengthHeader == null) return request;

        int contentLength = Integer.parseInt(contentLengthHeader.value);

        if (contentLength == 0) return request;

        // Read Body char by char
        CharArrayWriter wr = new CharArrayWriter();
        while (reader.ready() && wr.size() != contentLength) {
            int byte_ = reader.read();
            byte bt = (byte)byte_;
            wr.append((char) bt);
        }

        request.body = wr.toString();

        return request;
    }

    private void sendResponse(Socket client, BaseResponse response) throws IOException {
        byte[] responseBytes = response.sendable();
        OutputStream outputStream = client.getOutputStream();
        outputStream.write(responseBytes);
    }


}
