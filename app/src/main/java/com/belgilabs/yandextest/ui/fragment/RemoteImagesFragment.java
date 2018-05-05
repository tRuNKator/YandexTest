package com.belgilabs.yandextest.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.belgilabs.yandextest.AndroidUtilities;
import com.belgilabs.yandextest.R;
import com.belgilabs.yandextest.adapter.GridPhotoAdapter;
import com.belgilabs.yandextest.model.ImageModel;
import com.belgilabs.yandextest.network.ResourceLoaderTask;
import com.belgilabs.yandextest.network.rest.json.Resource;

import java.util.ArrayList;
import java.util.List;

public class RemoteImagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private GridPhotoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = view.findViewById(R.id.recycler_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), AndroidUtilities.calculateNoOfColumns(getContext())));
        adapter = new GridPhotoAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        syncWithDisk();

        return view;
    }

    private void syncWithDisk() {
        new ResourceLoaderTask(new ResourceLoaderTask.IProgressCallback() {
            @Override
            public void onResult(List<Resource> resources) {
                if (resources != null && !resources.isEmpty()) {
                    List<ImageModel> images = new ArrayList<>();
                    for (Resource resource : resources) {
                        if (resource.getMimeType().contains("image/")) {
                            images.add(new ImageModel(resource));
                        }
                    }

                    adapter.updateData(images);
                }
            }
        }).execute();
    }
}
