package Gekko;

import Gekko.Constants.Methods;
import Gekko.Constants.Values;
import Gekko.Interfaces.Handler;

import java.io.*;
import java.net.*;
import java.util.Hashtable;

public class GekkoServer {
    ServerSocket socket;
    Hashtable<String, Hashtable<String, Handler>> handlers = new Hashtable<>();
    int port;
    int backlog;
    int defaultClientTimeout = Values.defaultClientTimeout;
    boolean running = true;
    boolean debug;


    public GekkoServer() {
        this(Values.defaultPort, Values.defaultBacklog, false);
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
            // Services Request
            try {
                Socket client = socket.accept();
                client.setSoTimeout(defaultClientTimeout);
                Thread clientThread = new Thread( new Client(client, handlers));
                clientThread.start();
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

    private void exitWithError(String errorMessage) {
        System.err.println(errorMessage);
        System.exit(1);
    }

}
