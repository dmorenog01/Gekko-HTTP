package Gekko.Responses;

public class ServerErrorResponse extends BaseResponse {
    public ServerErrorResponse() {
        this.code = 500;
        this.message = "Server Error";
    }
}
