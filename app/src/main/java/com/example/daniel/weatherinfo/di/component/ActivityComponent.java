package com.example.daniel.weatherinfo.di.component;

import com.example.daniel.weatherinfo.di.PerActivity;
import com.example.daniel.weatherinfo.di.mudule.ActivityModule;
import com.example.daniel.weatherinfo.ui.activity.CityListActivity;
import com.example.daniel.weatherinfo.ui.activity.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(CityListActivity activity);
}
