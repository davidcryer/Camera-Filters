package com.davidcryer.camerafilters.screens.filter;

abstract class FilterMenuListenerForFilterUiListenerAdapter implements FilterMenu.Listener {
    private final FilterUi ui;

    FilterMenuListenerForFilterUiListenerAdapter(FilterUi ui) {
        this.ui = ui;
    }

    @Override
    public boolean onSelectColorEffect(String effect) {
        return hasListener() && listener().onClickMenuColorProcessing(ui, effect);
    }

    @Override
    public boolean onSelectImageEffect(String effect) {
        return hasListener() && listener().onClickMenuImageProcessing(ui, effect);
    }

    @Override
    public void onClickOnOffToggle() {
        if (hasListener()) listener().onClickOnOffToggle(ui);
    }

    abstract boolean hasListener();

    abstract FilterUi.Listener listener();
}
