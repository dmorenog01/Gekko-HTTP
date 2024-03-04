package Gekko.DefaultHandlers;

import Gekko.Interfaces.Handler;
import Gekko.Request;
import Gekko.Responses.BaseResponse;

public class HomeHandler implements Handler {
    @Override
    public BaseResponse getResponse(Request request, BaseResponse response) {
        response.setHTMLBody("<h1>Welcome to Gekko!</h1>");
        return response;
    }
}