package Gekko.DefaultHandlers;

import Gekko.*;
import Gekko.Interfaces.Handler;
import Gekko.Responses.BaseResponse;
import Gekko.Responses.BytesResponse;

import java.io.InputStream;


public class FaviconHandler implements Handler {

    byte[] imgBytes = null;


    public FaviconHandler() {
        try {
            InputStream stream = getClass().getResourceAsStream("/Gekko/InternalAssets/favicon.ico");
            if (stream == null) throw new Exception("Error while loading resource");
            imgBytes = stream.readAllBytes();
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
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
