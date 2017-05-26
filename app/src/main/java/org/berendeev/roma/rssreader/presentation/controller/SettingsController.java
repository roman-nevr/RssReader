package org.berendeev.roma.rssreader.presentation.controller;

import org.berendeev.roma.rssreader.BuildConfig;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.presentation.fragment.SettingsFragment;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsController {
    private final RssFeedRepository repository;
    private SettingsFragment fragment;
    private final CompositeDisposable compositeDisposable;
    private BaseRouter navigator;

    @Inject
    public SettingsController(RssFeedRepository repository) {

        this.repository = repository;

        compositeDisposable = new CompositeDisposable();
    }

    public void setNavigator(BaseRouter navigator){
        this.navigator = navigator;
    }

    public void setView(SettingsFragment fragment){
        this.fragment = fragment;
    }

    public void setNewUrl(String newUrl){
        try {
            URL url = new URL(newUrl);
            fragment.showProgress();
            compositeDisposable.add(repository
                    .setNewFeed(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        fragment.hideProgress();
                        fragment.showSuccess();
                        navigator.moveBack();
                    }, throwable -> {
                        fragment.hideProgress();
                        fragment.showNoRssError();
                    }));
        } catch (MalformedURLException e) {
            if (BuildConfig.DEBUG){
                e.printStackTrace();
            }
            fragment.hideProgress();
            fragment.showUrlError();
        }
    }

    public void stop(){
        compositeDisposable.dispose();
    }

    public void back(){
        navigator.moveBack();
    }
}
