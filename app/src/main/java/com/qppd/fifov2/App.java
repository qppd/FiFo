package com.qppd.fifov2;

import android.app.Application;

import net.gotev.speech.Logger;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Logger.setLogLevel(Logger.LogLevel.DEBUG);
    }
}
