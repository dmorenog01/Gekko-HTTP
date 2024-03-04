![Gekko](readmeAssets/GekkoLogo.png)

Gekko is a simple http server that can listen for requests on a specific port, parse them and return an appropriate response.

## Features
- Parse HTTP Request in to a convenient Request object with easily accessible headers, request path, method and body.
- Return response objects with different formats.
- Configure request handlers for specific paths / methods.

## Minimal Example

```java
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
```
