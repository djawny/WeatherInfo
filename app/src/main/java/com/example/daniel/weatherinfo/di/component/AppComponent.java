package com.example.daniel.weatherinfo.di.component;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.di.mudule.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MyApplication myApplication);
}
