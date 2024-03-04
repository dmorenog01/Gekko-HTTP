import Gekko.*;
import Gekko.DefaultHandlers.*;

public class Example {
    public static void main(String[] args) {
        try {
            GekkoServer server = new GekkoServer();
            server.addGET("/", new IndexHandler());
            server.addGET("/home", new HomeHandler());
            server.addGET("/favicon.ico", new FaviconHandler());
            server.runServer();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.exit(1);
        }
    }
}