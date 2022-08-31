package de.chloecdn.pteroclient.util;

public class RGBColor {

    private final int value;

    public RGBColor(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public RGBColor(int r, int g, int b, int a) {
        this.value = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
    }

    public RGBColor(int hex) {
        this.value = hex;
    }

    public int getValue() {
        return value;
    }
}