package Gekko.defaultHandlers;

import Gekko.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FaviconHandler implements Handler {
    File imgFile = null;
    byte[] imgBytes = null;


    public FaviconHandler() {
        String path = System.getProperty("user.dir") + "\\src\\Gekko\\images\\favicon.ico";
        imgFile = new File(path);
        if (!imgFile.exists()) {
            System.err.println("ERROR LOADING FILE");

        }
        try {
            imgBytes = Files.readAllBytes(imgFile.toPath());
        } catch (IOException e) {
            System.err.println("ERROR: " + e);
            System.exit(1);
        }
    }

    @Override
    public BaseResponse getResponse(Request request, BaseResponse response) {
        response = new BytesResponse(imgBytes);
        response.addHeader(new Header("Content-Type", "image/ico"));
        return response;
    }
}
