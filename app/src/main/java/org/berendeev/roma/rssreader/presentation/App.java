package org.berendeev.roma.rssreader.presentation;

import android.app.Application;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.berendeev.roma.rssreader.BuildConfig;
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
        initPicasso();
    }

    private void initDi() {
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule(this)).build();
    }

    public MainComponent getMainComponent(){
        return mainComponent;
    }

    private void initPicasso(){
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, getCacheMaxSize()));
        Picasso built = builder.build();
        if (BuildConfig.DEBUG){
            built.setIndicatorsEnabled(true);
            built.setLoggingEnabled(true);
        }
        Picasso.setSingletonInstance(built);
    }

    private int getCacheMaxSize(){
        return 10 * 1024 * 1024;
    }
}
