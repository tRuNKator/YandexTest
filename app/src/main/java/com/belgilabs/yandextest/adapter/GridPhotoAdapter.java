package com.belgilabs.yandextest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.belgilabs.yandextest.R;
import com.belgilabs.yandextest.model.ImageModel;
import com.belgilabs.yandextest.ui.activity.MainActivity;
import com.belgilabs.yandextest.ui.components.RadialProgressView;
import com.belgilabs.yandextest.ui.fragment.ImageViewerFragment;

import java.util.ArrayList;
import java.util.List;

import static com.belgilabs.yandextest.ui.activity.MainActivity.replaceView;

public class GridPhotoAdapter extends RecyclerView.Adapter {

    private final Context context;
    private List<ImageModel> images;

    private class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RadialProgressView progressView;
        ImageView image;

        ImageHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.item_img_small);
            this.progressView = itemView.findViewById(R.id.radialProgress);
            this.progressView.setIndeterminate(false);
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ImageViewerFragment fragment = ImageViewerFragment.newInstance((ArrayList<ImageModel>) images, getAdapterPosition());
            replaceView(fragment, ((MainActivity) context).getSupportFragmentManager(), true);
        }
    }

    public GridPhotoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_image, parent, false);

        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ImageHolder imageHolder = (ImageHolder) holder;
        ImageModel image = images.get(position);
        image.loadImageTo(imageHolder.image, imageHolder.progressView, false);
    }

    @Override
    public int getItemCount() {
        return (images != null) ? images.size() : 0;
    }

    public void updateData(List<ImageModel> images) {
        this.images = images;
        notifyDataSetChanged();
    }
}
