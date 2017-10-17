package com.daniel.jawny.weatherinfo.di.component;

import com.daniel.jawny.weatherinfo.MyApplication;
import com.daniel.jawny.weatherinfo.di.PerActivity;
import com.daniel.jawny.weatherinfo.di.mudule.ActivityModule;
import com.daniel.jawny.weatherinfo.ui.base.BaseActivity;
import com.daniel.jawny.weatherinfo.ui.locations.LocationsActivity;
import com.daniel.jawny.weatherinfo.ui.main.MainActivity;

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
