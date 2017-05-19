package org.berendeev.roma.rssreader.domain;

import org.berendeev.roma.rssreader.domain.entity.RssItem;

import java.net.URL;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface RssFeedRepository {

    Single<URL> getFeedUrl();

    Completable setNewFeed(URL url);

    Completable updateFeed();

    Observable<List<RssItem>> getFeedObservable();

    Completable clearOldFromDate(long date);
}
