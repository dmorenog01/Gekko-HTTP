package Gekko.DefaultHandlers;

import Gekko.Interfaces.Handler;
import Gekko.Request;
import Gekko.Responses.BaseResponse;

public class IndexHandler implements Handler {
    @Override
    public BaseResponse getResponse(Request request, BaseResponse response) {
        response.setJSONBody("{\"message\": \"Hello, Gekkos\"}");
        return response;
    }
}