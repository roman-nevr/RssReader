package org.berendeev.roma.rssreader.presentation;

import android.app.Application;

import org.berendeev.roma.rssreader.di.DaggerMainComponent;
import org.berendeev.roma.rssreader.di.MainComponent;
import org.berendeev.roma.rssreader.di.MainModule;


public class App extends Application {

    private static App instance;
    private MainComponent mainComponent;

    public static App getInstance() {
        return instance;
    }

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        initDi();

    }

    private void initDi() {
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule(this)).build();
    }

    public MainComponent getMainComponent(){
        return mainComponent;
    }
}
