import Gekko.*;
import Gekko.defaultHandlers.FaviconHandler;

import java.io.IOException;

class indexHandler implements Handler {
    @Override
    public BaseResponse getResponse(Request request, BaseResponse response) {
        response.setJSONBody("{\"message\": \"Hello, Gekkos\"}");
        return response;
    }
}

class homeHandler implements Handler {
    @Override
    public BaseResponse getResponse(Request request, BaseResponse response) {
        response.setHTMLBody("<h1>Welcome to Gekko!</h1>");
        return response;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            GekkoServer server = new GekkoServer();
            String[] methods = {"GET"};
            server.addHandler("/", methods, new indexHandler(), true);
            server.addHandler("/home", methods, new homeHandler(), true);
            server.addHandler("/favicon.ico", methods, new FaviconHandler(), true);
            server.runServer();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.exit(1);
        }
    }
}