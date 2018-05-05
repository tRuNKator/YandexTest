package com.belgilabs.yandextest.ui.components;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class FadeInImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int FADE_IN_TIME_MS = 250;

    public FadeInImageView(Context context) {
        super(context);
    }

    public FadeInImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FadeInImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                new ColorDrawable(getResources().getColor(android.R.color.transparent)),
                new BitmapDrawable(getContext().getResources(), bm)
        });

        setImageDrawable(td);
        td.startTransition(FADE_IN_TIME_MS);
    }
}
