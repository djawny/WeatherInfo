package com.example.daniel.weatherinfo.di.mudule;

import android.content.Context;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.DataManagerImpl;
import com.example.daniel.weatherinfo.data.database.Database;
import com.example.daniel.weatherinfo.data.database.DatabaseImpl;
import com.example.daniel.weatherinfo.data.network.OpenWeatherMapService;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private final MyApplication mApplication;
    private String mBaseUrl;

    public ApplicationModule(MyApplication application, String baseUrl) {
        mApplication = application;
        mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public MyApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Database provideDatabase(Context context) {
        return OpenHelperManager.getHelper(context, DatabaseImpl.class);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    @Singleton
    @Provides
    RxJava2CallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Singleton
    @Provides
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client, GsonConverterFactory converterFactory, RxJava2CallAdapterFactory adapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(adapterFactory)
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    OpenWeatherMapService provideOpenWeatherMapService(Retrofit retrofit) {
        return retrofit.create(OpenWeatherMapService.class);
    }

    @Singleton
    @Provides
    DataManager provideDataManager(DataManagerImpl dataManager) {
        return dataManager;
    }
}
