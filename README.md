![Gekko](readmeAssets/GekkoLogo.png)

Gekko is a simple http server that can listen for requests on a specific port, parse them and return an appropiate response.

## Features
- Parse HTTP Request in to a convenient Request object with easily accessible headers, request path, and method.
- Return response objects with different file formats.
- Configure request handlers for specific paths / methods.

## Minimal Example
```java
import Gekko.*;
import Gekko.defaultHandlers.FaviconHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String[] acceptedMethods = {"GET"};
            GekkoServer server = new GekkoServer();
            server.addHandler("/favicon.ico", acceptedMethods, new FaviconHandler());
            server.addHandler("/", acceptedMethods, new indexHandler());
            server.addHandler("/home", acceptedMethods, new homeHandler());
            server.runServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```
