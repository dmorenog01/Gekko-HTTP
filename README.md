![Gekko](readmeAssets/GekkoLogo.png)

# Introducing Gekko: Your Java HTTP Server Toolkit

Gekko is an educational Java library designed to streamline the creation and management of HTTP servers by leveraging Java's server socket API.

## Key Features:

- Automatic Request Parsing: Gekko automatically parses incoming requests, extracting essential information like method, path, headers, and body into a convenient Request object.
- Request Handling: Define request handlers using a simple interface with a getResponse method that takes the Request object and returns a corresponding Response object.
- Flexible Response Options: Easily build responses with text, JSON, or raw bytes through dedicated methods like setBody, setHTMLbody, and setJSONBody. These methods automatically set appropriate content-type headers.
- Binary Support: Handle binary responses like images efficiently using the BytesResponse class, which allows you to send raw byte data.


## Getting Started

This is a basic example of getting started with Gekko using the bundled request handlers.

```java
import Gekko.*;
import Gekko.DefaultHandlers.*;

public class Example {
    public static void main(String[] args) {
        GekkoServer server = new GekkoServer();
        server.addGET("/", new IndexHandler());
        server.addGET("/home", new HomeHandler());
        server.addGET("/favicon.icon", new FaviconHandler());
        server.runServer();
    }
}

```
