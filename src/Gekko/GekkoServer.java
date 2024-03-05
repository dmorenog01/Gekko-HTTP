package Gekko;

import Gekko.Constants.Methods;
import Gekko.Interfaces.Handler;
import Gekko.Responses.BaseResponse;
import Gekko.Responses.ServerErrorResponse;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class GekkoServer {
    ServerSocket socket;
    int port;
    int backlog;
    boolean running = true;
    boolean debug;
    Hashtable<String, Hashtable<String, Handler>> handlers = new Hashtable<>();

    public GekkoServer() {
        this(8080, 10, false);
    }

    public GekkoServer(boolean debug) {
        this(8080, 10, debug);
    }

    public GekkoServer(int port, int backlog, boolean debug) {
        this.port = port;
        this.backlog = backlog;
        this.debug = debug;
        try {
            socket = new ServerSocket(port, backlog);
        } catch (IllegalArgumentException e) {
            exitWithError("Invalid Port Number!");
        } catch (IOException e) {
            exitWithError("Error while creating socket: " + e.getMessage());
        } catch (Exception e) {
            exitWithError("Unknown Error: " + e.getMessage());
        }
    }

    public void runServer() {
        System.out.printf("Listening on port %d\n", this.port);
        System.out.println("Waiting for connections...");

        while (this.running) {
            // Services Request and closes connection to client
            try {
                Socket client = socket.accept();
                serviceRequest(client);
                client.close();
            } catch (Exception e) {
                System.err.println("ERROR: " + e.getMessage());
                System.exit(1);
            }
        }

        try {
            socket.close();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("ERROR while closing the server socket");
            System.exit(1);
        }

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

    public void addGET(String path, Handler handler) {
        addHandler(path, Methods.GET, handler);
    }

    private void serviceRequest(Socket client) {
        // Read request and pass it to appropriate request handler
        Request request = getRequest(client);

        if (debug) System.out.println("REQUEST:\n" + request);

        if (request.isNull) return;

        BaseResponse response = getResponse(request);

        if (debug) System.out.println("RESPONSE:\n" + response);

        sendResponse(client, response);
    }

    private BaseResponse getResponse(Request request) {
        Handler handler;

        Hashtable<String, Handler> pathHandler = this.handlers.get(request.path);
        if (pathHandler == null) return new ServerErrorResponse();

        handler = pathHandler.get(request.verb);
        if (handler == null) return new ServerErrorResponse();

        BaseResponse emptyResponse = new BaseResponse();
        return handler.getResponse(request, emptyResponse);
    }

    private Request getRequest(Socket client) {
        Request request;
        ArrayList<String> requestLines = new ArrayList<>();
        try {
            InputStream inputStream = client.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Read head
            String line = reader.readLine();
            while (line != null && !line.isBlank()) {
                requestLines.add(line);
                line = reader.readLine();
            }
            request = new Request(requestLines);

            Header contentLengthHeader = request.headers.get("Content-Length");
            if (contentLengthHeader == null) return request;

            int contentLength = Integer.parseInt(contentLengthHeader.value);
            if (contentLength == 0) return request;

            // Read Body char by char
            CharArrayWriter wr = new CharArrayWriter();
            while (reader.ready() && wr.size() != contentLength) {
                int byte_ = reader.read();
                byte bt = (byte) byte_;
                wr.append((char) bt);
            }

            request.body = wr.toString();
            return request;

        } catch (IOException e) {
            requestLines.clear();
            request = new Request(requestLines);
            return request;
        }

    }

    private void sendResponse(Socket client, BaseResponse response) {
        byte[] responseBytes = response.sendable();
        try {
            OutputStream outputStream = client.getOutputStream();
            outputStream.write(responseBytes);
        } catch (IOException e) {
            System.err.println("I/O Exception occurred while sending response. " + e.getMessage());
        }

    }

    private void exitWithError(String errorMessage) {
        System.err.println(errorMessage);
        System.exit(1);
    }

}
