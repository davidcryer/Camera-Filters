package com.davidcryer.camerafilters.screens.filter;

abstract class FilterMenuListenerForFilterUiListenerAdapter implements FilterMenu.Listener {
    private final FilterUi ui;

    FilterMenuListenerForFilterUiListenerAdapter(FilterUi ui) {
        this.ui = ui;
    }

    @Override
    public void onSelectColorEffect(String effect) {
        listener().onClickMenuColorProcessing(ui, effect);
    }

    @Override
    public void onSelectImageEffect(String effect) {
        listener().onClickMenuImageProcessing(ui, effect);
    }

    @Override
    public void onClickOnOffToggle() {
        listener().onClickOnOffToggle(ui);
    }

    @Override
    public void onClickTakePhoto() {
        listener().onClickTakePhotograph(ui);
    }

    abstract FilterUi.Listener listener();
}
