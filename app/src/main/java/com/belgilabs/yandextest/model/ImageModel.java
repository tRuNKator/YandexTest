package com.belgilabs.yandextest.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.belgilabs.yandextest.GlideApp;
import com.belgilabs.yandextest.YandexTestApplication;
import com.belgilabs.yandextest.network.rest.exceptions.NetworkIOException;
import com.belgilabs.yandextest.network.rest.exceptions.ServerIOException;
import com.belgilabs.yandextest.network.rest.json.Link;
import com.belgilabs.yandextest.network.rest.json.Resource;
import com.belgilabs.yandextest.ui.components.RadialProgressView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.Date;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageModel implements Parcelable {
    private String linkThumbnail;
    private String linkImage;
    private String remoteName;
    private Date createdTime;
    private Date modifiedTime;
    private long totalSize;
    private String resourcePath;
    private boolean isLoaded;

    public ImageModel(Resource resource) {
        this.resourcePath = resource.getPath().getPath();
        this.remoteName = resource.getName();
        this.createdTime = resource.getCreated();
        this.modifiedTime = resource.getModified();
        this.totalSize = resource.getSize();
        this.linkThumbnail = resource.getPreview();
    }

    protected ImageModel(Parcel in) {
        linkThumbnail = in.readString();
        linkImage = in.readString();
        remoteName = in.readString();
        totalSize = in.readLong();
        resourcePath = in.readString();
        isLoaded = in.readByte() != 0;
    }

    public void loadImageTo(@NonNull final ImageView imageView, @NonNull final RadialProgressView progressView, final boolean fitCenter) {
        try {
            if (isLoaded) {
                progressView.setVisibility(View.GONE);
                GlideApp.with(YandexTestApplication.applicationContext)
                        .load(new GlideUrl(linkImage) {
                            @Override
                            public String getCacheKey() {
                                return String.valueOf(totalSize)
                                        + String.valueOf(createdTime)
                                        + String.valueOf(modifiedTime)
                                        + resourcePath;
                            }
                        })
                        .apply(RequestOptions.centerCropTransform())
                        .into(imageView);
                return;
            }

            progressView.setVisibility(View.GONE);
            YandexTestApplication.getCloudApi().getDownloadLink(resourcePath).enqueue(new Callback<Link>() {
                @Override
                public void onResponse(@NonNull Call<Link> call, @NonNull Response<Link> response) {
                    Link body = response.body();
                    if ( body != null) {
                        progressView.setVisibility(View.VISIBLE);
                        linkImage = body.getHref();

                        ProgressManager.getInstance().addResponseListener(linkImage, new ProgressListener() {
                            @Override
                            public void onProgress(ProgressInfo progressInfo) {
                                float progress = ((float) progressInfo.getCurrentbytes())
                                        / ((float)progressInfo.getContentLength());
                                progressView.setProgress(progress, true);
                            }

                            @Override
                            public void onError(long id, Exception e) {

                            }
                        });

                        GlideApp.with(YandexTestApplication.applicationContext)
                                .load(new GlideUrl(linkImage) {
                                    @Override
                                    public String getCacheKey() {
                                        return String.valueOf(totalSize)
                                                + String.valueOf(createdTime)
                                                + String.valueOf(modifiedTime)
                                                + resourcePath;
                                    }
                                })
                                .thumbnail(GlideApp.with(YandexTestApplication.applicationContext)
                                                    .load(linkThumbnail)
                                                    .apply(RequestOptions.bitmapTransform(new BlurTransformation())))
                                .apply(fitCenter ? RequestOptions.fitCenterTransform() :RequestOptions.centerCropTransform())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        progressView.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        isLoaded = true;
                                        progressView.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(imageView);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Link> call, @NonNull Throwable t) {

                }
            });
        } catch (NetworkIOException e) {
            e.printStackTrace();
        } catch (ServerIOException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(linkThumbnail);
        dest.writeString(linkImage);
        dest.writeString(remoteName);
        dest.writeLong(totalSize);
        dest.writeString(resourcePath);
        dest.writeByte((byte) (isLoaded ? 1 : 0));
    }
}
