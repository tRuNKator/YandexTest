package com.belgilabs.yandextest.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.belgilabs.yandextest.R;
import com.belgilabs.yandextest.adapter.ImageViewerAdapter;
import com.belgilabs.yandextest.model.ImageModel;
import com.belgilabs.yandextest.ui.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class ImageViewerFragment extends Fragment {

    private ViewPager imagesPager;
    private List<ImageModel> images;

    public static ImageViewerFragment newInstance(ArrayList<ImageModel> images, int current) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("yandextest.imageslist", images);
        bundle.putInt("yandextest.currentimage", current);
        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_viewer, container, false);
        imagesPager = view.findViewById(R.id.vp_images);
        int current = 0;
        if (getArguments() != null) {
            images = getArguments().getParcelableArrayList("yandextest.imageslist");
            current = getArguments().getInt("yandextest.currentimage");
        }
        ImageViewerAdapter pagerAdapter = new ImageViewerAdapter(getContext(), images);
        imagesPager.setAdapter(pagerAdapter );
        imagesPager.setPageTransformer(false, new DepthPageTransformer());
        imagesPager.setCurrentItem(current);
        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, final boolean enter, int nextAnim) {
        if (enter && nextAnim == R.anim.open_enter) {
            Animation nextAnimation = AnimationUtils.loadAnimation(getContext(), nextAnim);
            nextAnimation.setAnimationListener(new Animation.AnimationListener() {

                private float mOldTranslationZ;

                @Override
                public void onAnimationStart(Animation animation) {
                    if (getView() != null) {
                        mOldTranslationZ = ViewCompat.getTranslationZ(getView());
                        ViewCompat.setTranslationZ(getView(), 100.f);
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (getView() != null) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ViewCompat.setTranslationZ(getView(), mOldTranslationZ);
                            }
                        }, 100);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            return nextAnimation;
        }

        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
