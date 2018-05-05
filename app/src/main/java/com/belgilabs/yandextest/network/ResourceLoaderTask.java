package com.belgilabs.yandextest.network;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.belgilabs.yandextest.network.rest.ResourcesArgs;
import com.belgilabs.yandextest.network.rest.ResourcesHandler;
import com.belgilabs.yandextest.network.rest.RestClient;
import com.belgilabs.yandextest.network.rest.json.Resource;
import com.belgilabs.yandextest.network.rest.json.ResourceList;

import java.util.ArrayList;
import java.util.List;

public class ResourceLoaderTask extends AsyncTask<Void, List<Resource>, List<Resource>> {
    private static final String TAG = ResourceLoaderTask.class.getSimpleName();

    private static final int ITEMS_PER_REQUEST = 20;

    private List<Resource> filesInfo;
    private boolean hasCanceled;
    private IProgressCallback callback;

    public interface IProgressCallback {
        void onResult(List<Resource> resources);
    }

    public ResourceLoaderTask(IProgressCallback callback) {
        this.callback = callback;
    }

    @Override
    protected List<Resource> doInBackground(Void... voids) {
        filesInfo = new ArrayList<>();
        hasCanceled = false;

        int offset = 0;

        try {
            RestClient restClient = new RestClient();
            int size;
            do {
                ResourceList resourceList = restClient.getFlatResourceList(
                        new ResourcesArgs.Builder()
                                .setLimit(ITEMS_PER_REQUEST)
                                .setMediaType("image")
                                .setOffset(offset)
                                .setSort(ResourcesArgs.Sort.name)
                                .setParsingHandler(new ResourcesHandler() {
                                    @Override
                                    public void handleItem(Resource item) {
                                        filesInfo.add(item);
                                    }
                                })
                                .build());
                offset += ITEMS_PER_REQUEST;

                size = resourceList.getItems().size();
            } while (!hasCanceled && size >= ITEMS_PER_REQUEST);
            return filesInfo;

        } catch (Exception e) {
            Log.e(TAG, "doInBackground", e);
        }

        return filesInfo;
    }

    @Override
    protected void onPostExecute(List<Resource> resources) {
        super.onPostExecute(resources);
        if (callback != null) {
            callback.onResult(resources);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        hasCanceled = true;
    }
}
