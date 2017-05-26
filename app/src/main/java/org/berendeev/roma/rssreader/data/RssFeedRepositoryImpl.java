package org.berendeev.roma.rssreader.data;

import org.berendeev.roma.rssreader.data.http.RssFeedHttpDataSource;
import org.berendeev.roma.rssreader.data.preferences.PreferencesDataSources;
import org.berendeev.roma.rssreader.data.sqlite.RssFeedSqlDataSource;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.domain.entity.RssItem;

import java.net.URL;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class RssFeedRepositoryImpl implements RssFeedRepository {

    private PreferencesDataSources preferencesDataSources;
    private RssFeedSqlDataSource sqlDataSource;
    private RssFeedHttpDataSource httpDataSource;

    private BehaviorSubject<List<RssItem>> rssSubject;
    private URL url;

    public RssFeedRepositoryImpl(PreferencesDataSources preferencesDataSources, RssFeedSqlDataSource sqlDataSource, RssFeedHttpDataSource httpDataSource) {
        this.preferencesDataSources = preferencesDataSources;
        this.sqlDataSource = sqlDataSource;
        this.httpDataSource = httpDataSource;

        rssSubject = BehaviorSubject.createDefault(sqlDataSource.getAllRssItems());
        url = getFeedUrl().blockingGet();
        if (sqlDataSource.isEmpty()){
            updateFeed()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }
    }

    @Override public Single<URL> getFeedUrl() {
        return Single.fromCallable(() ->
            new URL(preferencesDataSources.getLink())
        );
    }

    @Override public Completable setNewFeed(URL url) {
        return Completable.fromAction(() -> {
            if (httpDataSource.isRssAvailable(url)){
                RssFeedRepositoryImpl.this.url = url;
                String name = httpDataSource.getFeedName(url);
                preferencesDataSources.saveNewFeed(url.toString(), name);
                sqlDataSource.removeAll();
                updateFeedInner();
            }else {
                throw new IllegalArgumentException("rss not found");
            }
        });
    }

    @Override public Completable updateFeed() {
        return Completable.fromAction(() -> {
            updateFeedInner();
        });
    }

    private void updateFeedInner(){
        List<RssItem> rssItems = httpDataSource.getFeed(url);
        sqlDataSource.saveAllRssItems(rssItems);
        rssItems = sqlDataSource.getAllRssItems();
        rssSubject.onNext(rssItems);
    }

    @Override public Single<RssItem> getRssItem(String link) {
        return Single.fromCallable(() -> {
            RssItem rssItem = sqlDataSource.getRssItem(link);
            return rssItem;
            });
    }

    @Override public Observable<List<RssItem>> getFeedObservable() {
        return rssSubject;
    }

    @Override public Completable clearOldFromDate(long date) {
        return null;
    }

    @Override public String getChannelName() {
//        return Single.fromCallable(() -> {
//            String name = preferencesDataSources.getName();
//            return name;
//        });
        return preferencesDataSources.getName();
    }


}
