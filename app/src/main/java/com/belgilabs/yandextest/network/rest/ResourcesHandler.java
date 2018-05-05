package com.belgilabs.yandextest.network.rest;

import com.belgilabs.yandextest.network.rest.json.Resource;

public abstract class ResourcesHandler {

    public void handleSelf(Resource item) {
    }

    public void handleItem(Resource item) {
    }

    public void onFinished(int itemsOnPage) {
    }
}

