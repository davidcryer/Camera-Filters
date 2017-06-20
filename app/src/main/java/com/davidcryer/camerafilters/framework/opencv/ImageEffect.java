package com.davidcryer.camerafilters.framework.opencv;

public enum ImageEffect {
    NONE, ZOOM, PIXELISE, POSTERISE;

    public static ImageEffect effect(String effect) {
        switch (effect) {
            case "None": {
                return NONE;
            }
            case "Zoom": {
                return ZOOM;
            }
            case "Pixelise": {
                return PIXELISE;
            }
            case "Posterise": {
                return POSTERISE;
            }
        }
        return null;
    }
}
