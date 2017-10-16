package com.example.daniel.weatherinfo.di.component;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.di.PerActivity;
import com.example.daniel.weatherinfo.di.mudule.ActivityModule;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;
import com.example.daniel.weatherinfo.ui.locations.LocationsActivity;
import com.example.daniel.weatherinfo.ui.main.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(LocationsActivity activity);

    final class Initializer {
        public static ActivityComponent init(BaseActivity baseActivity) {
            return DaggerActivityComponent.builder()
                    .applicationComponent(MyApplication.getComponent(baseActivity))
                    .activityModule(new ActivityModule(baseActivity))
                    .build();
        }
    }
}
