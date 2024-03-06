package Gekko;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class Request {
    String verb; //Change to Enum
    String path;
    Hashtable<String, Header> headers = new Hashtable<>();
    String protocol;
    String body = "";
    boolean isNull = false;

    ArrayList<String> rawRequest;

    public Request(ArrayList<String> requestLines) {
        rawRequest = requestLines;
        if (requestLines.isEmpty()) isNull = true;
        parseRequest();
    }

    private void parseRequest() {
        String line;
        String[] splitline;
        for (int i = 0; i < this.rawRequest.size(); i++) {
            line = this.rawRequest.get(i);
            if (i == 0) {
                splitline = line.split(" ");
                if (splitline.length != 3) {
                    // Malformed request
                    this.isNull = true;
                    return;
                }
                this.verb = splitline[0];
                this.path = splitline[1];
                this.protocol = splitline[2];
            } else {
                splitline = line.split(":", 2);
                Header header = new Header(splitline[0].strip(), splitline[1].strip());
                headers.put(header.key, header);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("%s %s %s\n".formatted(this.verb, this.path, this.protocol));
        Set<String> setOfKeys = headers.keySet();
        for (String key : setOfKeys) {
            builder.append("%s\n".formatted(headers.get(key).toString()));
        }
        builder.append("\n");
        if (!this.body.isEmpty()) {
            builder.append(this.body);
            builder.append("\n");
        }
        return builder.toString();
    }
}
