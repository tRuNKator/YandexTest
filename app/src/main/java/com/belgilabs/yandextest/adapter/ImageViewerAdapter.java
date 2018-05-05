package com.belgilabs.yandextest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.belgilabs.yandextest.R;
import com.belgilabs.yandextest.model.ImageModel;
import com.belgilabs.yandextest.ui.components.FadeInImageView;
import com.belgilabs.yandextest.ui.components.RadialProgressView;

import java.util.List;

public class ImageViewerAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private List<ImageModel> images;

    public ImageViewerAdapter(Context context, List<ImageModel> images) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.page_image, container, false);

        FadeInImageView imageView =  itemView.findViewById(R.id.image_full);
        RadialProgressView progressView =  itemView.findViewById(R.id.loading_progress);
        images.get(position).loadImageTo(imageView, progressView, true);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }

}
