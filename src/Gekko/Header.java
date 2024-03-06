package Gekko;

public class Header {
    public String key;
    public String value;

    public Header(String k, String v) {
        key = k;
        value = v;
    }

    @Override
    public String toString() {
        return key + ": " + value;
    }
}
