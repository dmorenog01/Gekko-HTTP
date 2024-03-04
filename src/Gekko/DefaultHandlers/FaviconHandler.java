package Gekko.DefaultHandlers;

import Gekko.*;
import Gekko.Interfaces.Handler;
import Gekko.Responses.BaseResponse;
import Gekko.Responses.BytesResponse;

import java.io.IOException;
import java.io.InputStream;


public class FaviconHandler implements Handler {

    byte[] imgBytes = null;


    public FaviconHandler() throws IOException {

        try (InputStream stream = getClass().getResourceAsStream("/Gekko/InternalAssets/favicon.ico")) {
            if (stream != null) {
                imgBytes = stream.readAllBytes();
            } else {
            System.err.println("ERROR LOADING FILE");
            System.exit(1);
        }}
    }

    @Override
    public BaseResponse getResponse(Request request, BaseResponse response) {
        response = new BytesResponse(imgBytes);
        response.addHeader(new Header("Content-Type", "image/ico"));
        return response;
    }
}
