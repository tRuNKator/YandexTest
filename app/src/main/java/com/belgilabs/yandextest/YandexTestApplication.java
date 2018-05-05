package com.belgilabs.yandextest;

import android.content.Context;
import android.support.annotation.NonNull;

import com.belgilabs.yandextest.network.rest.CloudApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YandexTestApplication extends android.app.Application {

    public static volatile Context applicationContext;
    private static OkHttpClient mOkHttpClient;

    private static final int CONNECT_TIMEOUT_MILLIS = 30 * 1000;
    private static final int READ_TIMEOUT_MILLIS = 30 * 1000;
    private static final int WRITE_TIMEOUT_MILLIS = 30 * 1000;

    private static CloudApi cloudApi;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        AppPreferences.initialize(applicationContext);

        mOkHttpClient = ProgressManager.getInstance()
                .with(
                        new OkHttpClient.Builder()
                        .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                        .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                        .writeTimeout(WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                        .followSslRedirects(true)
                        .followRedirects(true)
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(@NonNull Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .header("Authorization", "OAuth " +
                                                AppPreferences.getInstance().getString(Constants.PREF_TOKEN))
                                        .header("User-Agent","Cloud API Android Client Example/1.0")
                                        .build();
                                return chain.proceed(request);
                            }
                        })
                )
                .build();


        cloudApi = new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CloudApi.class);
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static CloudApi getCloudApi() {
        return cloudApi;
    }
}
