package com.example.daniel.weatherinfo.di.component;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.di.mudule.ApplicationModule;
import com.example.daniel.weatherinfo.di.mudule.DataManagerModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DataManagerModule.class})
public interface ApplicationComponent {

    void inject(MyApplication myApplication);

    DataManager exposeDataManager();
}
