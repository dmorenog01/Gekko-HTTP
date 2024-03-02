import Gekko.GekkoServer;

public class Main {
    public static void main(String[] args) {
        try {
            GekkoServer server = new GekkoServer();
            server.runServer();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}