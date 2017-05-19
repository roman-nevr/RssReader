package org.berendeev.roma.rssreader.data;

import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.domain.entity.RssItem;

import java.net.URL;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public class RssFeedRepositoryImpl implements RssFeedRepository {
    @Override public Single<URL> getFeedUrl() {
        return null;
    }

    @Override public Completable setNewFeed(URL url) {
        return null;
    }

    @Override public Completable updateFeed() {
        return null;
    }

    @Override public Observable<List<RssItem>> getFeedObservable() {
        return null;
    }

    @Override public Completable clearOldFromDate(long date) {
        return null;
    }


}
