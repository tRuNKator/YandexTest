package com.belgilabs.yandextest.ui.components;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.belgilabs.yandextest.AndroidUtilities;

public class RadialProgressView extends View {

    private static final int DEF_PROGRESS_COLOR = 0xFF527DA3;
    private static final int DEF_SIZE = 40;
    private static final int DEF_DIFF = 3;
    private static final int DEF_LINE_WIDTH = 3;

    private static final float rotationTime = 3000;
    private static final float risingTime = 600;

    private static DecelerateInterpolator decelerateInterpolator;
    private static AccelerateInterpolator accelerateInterpolator;

    private long lastUpdateTime;
    private float radOffset;
    private float currentProgress;
    private float animationProgressStart;
    private long currentProgressTime;
    private float animatedProgressValue;
    private RectF circleRect = new RectF();
    private float animatedAlphaValue = 1.0f;

    private Paint progressPaint;
    private int progressColor = DEF_PROGRESS_COLOR;
    private int diff;
    private int whSize;

    private float currentCircleLength;
    private boolean risingCircleLength;
    private boolean indeterminate = true;

    public RadialProgressView(@NonNull Context context) {
        this(context, null);
    }

    public RadialProgressView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadialProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadialProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        AndroidUtilities.checkDisplaySize(context);
        diff = AndroidUtilities.dp2px(DEF_DIFF);
        if (decelerateInterpolator == null) {
            decelerateInterpolator = new DecelerateInterpolator();
            accelerateInterpolator = new AccelerateInterpolator();
        }
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(AndroidUtilities.dp2px(DEF_LINE_WIDTH));
        progressPaint.setColor(progressColor);
    }

    public void setStrokeWidth(int width) {
        progressPaint.setStrokeWidth(width);
    }

    private void updateAnimation() {
        long newTime = System.currentTimeMillis();
        long dt = newTime - lastUpdateTime;
        if (dt > 17) {
            dt = 17;
        }
        lastUpdateTime = newTime;

        if (!indeterminate) {
            if (animatedProgressValue != 1) {
                radOffset += 360 * dt / rotationTime;
                float progressDiff = currentProgress - animationProgressStart;
                if (progressDiff > 0) {
                    currentProgressTime += dt;
                    if (currentProgressTime >= 300) {
                        animatedProgressValue = currentProgress;
                        animationProgressStart = currentProgress;
                        currentProgressTime = 0;
                    } else {
                        animatedProgressValue = animationProgressStart + progressDiff * decelerateInterpolator.getInterpolation(currentProgressTime / 300.0f);
                    }
                }
            }

            if (animatedProgressValue >= 1) {
                animatedAlphaValue -= dt / 200.0f;
                if (animatedAlphaValue <= 0) {
                    animatedAlphaValue = 0.0f;
                }
            }
        } else {
            radOffset += 360 * dt / rotationTime;
            int count = (int) (radOffset / 360);
            radOffset -= count * 360;

            currentProgressTime += dt;
            if (currentProgressTime >= risingTime) {
                currentProgressTime = (long) risingTime;
            }
            if (risingCircleLength) {
                currentCircleLength = 4 + 266 * accelerateInterpolator.getInterpolation(currentProgressTime / risingTime);
            } else {
                currentCircleLength = 4 - 270 * (1.0f - decelerateInterpolator.getInterpolation(currentProgressTime / risingTime));
            }
            if (currentProgressTime == risingTime) {
                if (risingCircleLength) {
                    radOffset += 270;
                    currentCircleLength = -266;
                }
                risingCircleLength = !risingCircleLength;
                currentProgressTime = 0;
            }

        }
        invalidate();
    }

    public void setProgressColor(int color) {
        progressColor = color;
        progressPaint.setColor(color);
    }

    public void setProgress(float value, boolean animated) {
        if (!animated) {
            animatedProgressValue = value;
            animationProgressStart = value;
        } else {
            if (animatedProgressValue > value) {
                animatedProgressValue = value;
            }
            animationProgressStart = animatedProgressValue;
        }
        currentProgress = value;
        currentProgressTime = 0;

        invalidate();
    }

    public float getProgress() {
        return indeterminate ? 0 : currentProgress;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    public void resetProgress() {
        animatedAlphaValue = 1.0f;
        setProgress(1.0f, false);
    }

    private int measureSize(int measureSpec) {
        int defSize = AndroidUtilities.dp2px(DEF_SIZE);
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        int result = 0;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                result = Math.min(defSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp2px(54), MeasureSpec.EXACTLY));
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int whMin = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingBottom() - getPaddingTop());
        whSize = whMin - (diff * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        progressPaint.setAlpha((int) (255 * animatedAlphaValue));

        int x = (getMeasuredWidth() - whSize) / 2;
        int y = (getMeasuredHeight() - whSize) / 2;
        circleRect.set(x, y, whSize + x, whSize + y);

        if (indeterminate) {
            canvas.drawArc(circleRect, radOffset, currentCircleLength, false, progressPaint);
        } else {
            canvas.drawArc(circleRect, -90 + radOffset, Math.max(4, 360 * animatedProgressValue), false, progressPaint);
        }

        updateAnimation();
    }

}
