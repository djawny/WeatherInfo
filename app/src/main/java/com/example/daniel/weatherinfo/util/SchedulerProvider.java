package com.example.daniel.weatherinfo.util;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler io();
}
