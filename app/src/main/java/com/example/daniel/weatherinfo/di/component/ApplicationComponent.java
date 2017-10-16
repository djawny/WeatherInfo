package com.example.daniel.weatherinfo.di.component;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.di.mudule.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApplication myApplication);

    DataManager exposeDataManager();

    final class Initializer {
        public static ApplicationComponent init(MyApplication myApplication) {
            return DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(myApplication))
                    .build();
        }
    }
}
