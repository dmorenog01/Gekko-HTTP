package Gekko.Interfaces;

import Gekko.Request;
import Gekko.Responses.BaseResponse;

public interface Handler {
    BaseResponse getResponse (Request request, BaseResponse response);
}
