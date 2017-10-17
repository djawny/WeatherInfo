package com.daniel.jawny.weatherinfo.di.mudule;

import android.content.Context;

import com.daniel.jawny.weatherinfo.MyApplication;
import com.daniel.jawny.weatherinfo.data.DataManager;
import com.daniel.jawny.weatherinfo.data.DataManagerImpl;
import com.daniel.jawny.weatherinfo.data.database.Database;
import com.daniel.jawny.weatherinfo.data.database.DatabaseImpl;
import com.daniel.jawny.weatherinfo.data.network.OpenWeatherMapService;
import com.daniel.jawny.weatherinfo.data.prefs.Preferences;
import com.daniel.jawny.weatherinfo.data.prefs.PreferencesImpl;
import com.daniel.jawny.weatherinfo.di.ApplicationContext;
import com.daniel.jawny.weatherinfo.util.AppConstants;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
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

    public ApplicationModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    MyApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Database provideDatabase(@ApplicationContext Context context) {
        return OpenHelperManager.getHelper(context, DatabaseImpl.class);
    }

    @Provides
    @Named("preferencesName")
    String providePreferencesName() {
        return AppConstants.PREFERENCES_NAME;
    }

    @Provides
    @Singleton
    Preferences provideSharedPreferences(PreferencesImpl preferencesImpl) {
        return preferencesImpl;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(AppConstants.OK_HTTP_TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(AppConstants.OK_HTTP_TIMEOUT_SEC, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Named("openWeatherMapBaseUrl")
    String provideBaseUrl() {
        return AppConstants.OWM_BASE_URL;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, GsonConverterFactory converterFactory,
                             RxJava2CallAdapterFactory adapterFactory, @Named("openWeatherMapBaseUrl") String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(adapterFactory)
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    OpenWeatherMapService provideOpenWeatherMapService(Retrofit retrofit) {
        return retrofit.create(OpenWeatherMapService.class);
    }

    @Provides
    @Singleton
    DataManager provideDataManager(DataManagerImpl dataManagerImpl) {
        return dataManagerImpl;
    }
}
