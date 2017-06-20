package com.davidcryer.camerafilters.framework.opencv;

public enum ColorEffect {
    RGBA, BRGA, GREY, CANNY;

    public static ColorEffect effect(String effect) {
        switch (effect) {
            case "RGBA": {
                return RGBA;
            }
            case "BRGA": {
                return BRGA;
            }
            case "Grey": {
                return GREY;
            }
            case "Canny": {
                return CANNY;
            }
        }
        return null;
    }
}
