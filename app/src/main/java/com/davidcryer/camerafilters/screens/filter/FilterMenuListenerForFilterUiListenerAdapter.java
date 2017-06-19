package com.davidcryer.camerafilters.screens.filter;

import com.davidcryer.camerafilters.framework.opencv.ImageManipulator;

abstract class FilterMenuListenerForFilterUiListenerAdapter implements FilterMenu.Listener {
    private final FilterUi ui;

    FilterMenuListenerForFilterUiListenerAdapter(FilterUi ui) {
        this.ui = ui;
    }

    @Override
    public void onClickStart() {
        if (hasListener()) listener().onClickStart(ui);
    }

    @Override
    public void onClickStop() {
        if (hasListener()) listener().onClickStop(ui);
    }

    @Override
    public void onClickRgba() {
        if (hasListener()) listener().onClickMenuColorProcessing(ui, ImageManipulator.ColorProcessing.RGBA);
    }

    @Override
    public void onClickBrga() {
        if (hasListener()) listener().onClickMenuColorProcessing(ui, ImageManipulator.ColorProcessing.BRGA);
    }

    @Override
    public void onClickGrey() {
        if (hasListener()) listener().onClickMenuColorProcessing(ui, ImageManipulator.ColorProcessing.GREY);
    }

    @Override
    public void onClickCanny() {
        if (hasListener()) listener().onClickMenuColorProcessing(ui, ImageManipulator.ColorProcessing.CANNY);
    }

    abstract boolean hasListener();

    abstract FilterUi.Listener listener();
}
