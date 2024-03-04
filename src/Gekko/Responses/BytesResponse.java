package Gekko.Responses;

import Gekko.Header;

public class BytesResponse extends BaseResponse {
    byte[] bytesBody;

    public BytesResponse(byte[] body) {
        this.bytesBody = body;
        String contentLength = "%d".formatted(bytesBody.length);
        Header contentLengthHeader = headers.get("Content-Length");
        if (contentLengthHeader != null) {
            contentLengthHeader.value = contentLength;
        } else {
            addHeader(new Header("Content-Length", contentLength));
        }
    }

    @Override
    public byte[] sendable() {
        byte[] firstPart = super.sendable();
        byte[] combinedResponse = new byte[firstPart.length + bytesBody.length];

        System.arraycopy(firstPart, 0, combinedResponse, 0, firstPart.length);
        System.arraycopy(bytesBody, 0, combinedResponse, firstPart.length, bytesBody.length);

        return combinedResponse;
    }
}
