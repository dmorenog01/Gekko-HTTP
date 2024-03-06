package Gekko.Responses;

import Gekko.Header;

import java.util.Hashtable;
import java.util.Set;

public class BaseResponse {
    String protocol;
    short code;
    String message;
    Hashtable<String, Header> headers = new Hashtable<>();
    private String body;

    public BaseResponse() {
        this.protocol = "HTTP/1.0";
        this.code = 200;
        this.message = "OK";
        addHeader(new Header("Server", "Gekko"));
    }

    public void addHeader(Header header) {
        this.headers.put(header.key, header);
    }

    public void setBody(String body) {
        String contentLength = "%d".formatted(body.length());
        this.body = body;
        Header contentLengthHeader = headers.get("Content-Length");
        if (contentLengthHeader != null) {
            contentLengthHeader.value = contentLength;
        } else {
            addHeader(new Header("Content-Length", contentLength));
        }
    }

    public void setHTMLBody(String body) {
        setBody(body);
        addHeader(new Header("Content-Type", "text/html"));
    }

    public void setJSONBody(String body) {
        setBody(body);
        addHeader(new Header("Content-Type", "application/json"));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("%s %d %s\n".formatted(this.protocol, this.code, this.message));
        Set<String> setOfKeys = headers.keySet();
        for (String key : setOfKeys) {
            builder.append("%s\n".formatted(headers.get(key).toString()));
        }
        builder.append("\n");
        if (this.body != null) {
            builder.append(this.body);
        }
        return builder.toString();
    }

    public byte[] sendable() {
        return this.toString().getBytes();
    }
}
