import Gekko.GekkoServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        try {
            GekkoServer server = new GekkoServer();
            server.runServer();
//        } catch (Exception e) {
//            System.out.println("ERROR: " + e.getMessage());
//        }
    }
}