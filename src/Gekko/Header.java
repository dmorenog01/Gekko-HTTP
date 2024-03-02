package Gekko;

public class Header {
    String key;
    String value;
    public Header(String k, String v) {
        key = k;
        value = v;
    }

    @Override
    public String toString() {
        return key + ": " + value;
    }
}
