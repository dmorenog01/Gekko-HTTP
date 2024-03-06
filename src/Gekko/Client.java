package Gekko;

import Gekko.Interfaces.Handler;
import Gekko.Responses.BaseResponse;
import Gekko.Responses.ServerErrorResponse;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Client implements Runnable{
    Socket clientSocket;
    boolean debug;
    Hashtable<String, Hashtable<String, Handler>> handlers;
    public Client(Socket clientSocket, Hashtable<String, Hashtable<String, Handler>> handlers) {
            this(clientSocket, handlers, false);
    }

    public Client(Socket clientSocket, Hashtable<String, Hashtable<String, Handler>> handlers, boolean debug) {
        this.clientSocket = clientSocket;
        this.handlers = handlers;
        this.debug = debug;
    }
    @Override
    public void run() {
        try (Socket socket = clientSocket) {
            serviceRequest();
        } catch (SocketTimeoutException e) {
            System.err.println("Socket Timed out. Closing...");
        }
        catch (IOException e) {
            System.err.println("ERROR closing socket: " + e.getMessage());
        }
    }

    private void serviceRequest() throws SocketTimeoutException {
        // Read request and pass it to appropriate request handler
        Request request = getRequest(clientSocket);

        if (debug) System.out.println("REQUEST:\n" + request);

        if (request.isNull) return;

        BaseResponse response = getResponse(request);

        if (debug) System.out.println("RESPONSE:\n" + response);

        sendResponse(clientSocket, response);
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

    private Request getRequest(Socket client) throws SocketTimeoutException {
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

        } catch (SocketTimeoutException e) {
            throw e;
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

}
