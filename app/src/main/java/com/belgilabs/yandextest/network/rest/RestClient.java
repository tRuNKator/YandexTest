package com.belgilabs.yandextest.network.rest;

import com.belgilabs.yandextest.YandexTestApplication;
import com.belgilabs.yandextest.network.rest.exceptions.ServerIOException;
import com.belgilabs.yandextest.network.rest.json.Resource;
import com.belgilabs.yandextest.network.rest.json.ResourceList;

import java.io.IOException;
import java.util.List;

public class RestClient {
    public Resource getResources(final ResourcesArgs args)
            throws IOException, ServerIOException {
        CloudApi cloudApi = YandexTestApplication.getCloudApi();
        final Resource resource = cloudApi.getResources(args.getPath(), args.getFields(),
                args.getLimit(), args.getOffset(), args.getSort(), args.getPreviewSize(),
                args.getPreviewCrop()).execute().body();
        if (args.getParsingHandler() != null) {
            parseListResponse(resource, args.getParsingHandler());
        }
        return resource;
    }

    public ResourceList getFlatResourceList(final ResourcesArgs args)
            throws IOException, ServerIOException {
        CloudApi cloudApi = YandexTestApplication.getCloudApi();
        final ResourceList resourceList = cloudApi.getFlatResourceList(args.getLimit(), args.getMediaType(),
                args.getOffset(), args.getFields(), args.getPreviewSize(), args.getPreviewCrop()).execute().body();
        if (args.getParsingHandler() != null) {
            parseListResponse(resourceList, args.getParsingHandler());
        }
        return resourceList;
    }

    private void parseListResponse(final Resource resource, final ResourcesHandler handler) {
        handler.handleSelf(resource);
        ResourceList items = resource.getResourceList();
        int size = 0;
        if (items != null) {
            size = items.getItems().size();
            for (Resource item : items.getItems()) {
                handler.handleItem(item);
            }
        }
        handler.onFinished(size);
    }

    private void parseListResponse(final ResourceList resourceList, final ResourcesHandler handler) {
        List<Resource> items = resourceList.getItems();
        int size = 0;
        if (items != null) {
            size = items.size();
            for (Resource item : items) {
                handler.handleItem(item);
            }
        }
        handler.onFinished(size);
    }

}
