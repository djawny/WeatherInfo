package com.daniel.jawny.weatherinfo.di.component;

import android.app.Activity;

import com.daniel.jawny.weatherinfo.MyApplication;
import com.daniel.jawny.weatherinfo.di.PerActivity;
import com.daniel.jawny.weatherinfo.di.mudule.ActivityModule;
import com.daniel.jawny.weatherinfo.ui.base.BaseActivity;
import com.daniel.jawny.weatherinfo.ui.locations.LocationsActivity;
import com.daniel.jawny.weatherinfo.ui.main.MainActivity;
import com.daniel.jawny.weatherinfo.ui.main.current.CurrentFragment;
import com.daniel.jawny.weatherinfo.ui.main.forecast.ForecastFragment;
import com.daniel.jawny.weatherinfo.ui.main.map.MapFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(LocationsActivity activity);

    void inject(CurrentFragment fragment);

    void inject(ForecastFragment fragment);

    void inject(MapFragment fragment);

    final class Initializer {
        public static ActivityComponent init(Activity activity) {
            return DaggerActivityComponent.builder()
                    .applicationComponent(MyApplication.getComponent(activity))
                    .activityModule(new ActivityModule(activity))
                    .build();
        }
    }
}
