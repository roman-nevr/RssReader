package org.berendeev.roma.rssreader.di;

import android.content.Context;

import org.berendeev.roma.rssreader.data.HtmlImageFillerImpl;
import org.berendeev.roma.rssreader.data.RssFeedRepositoryImpl;
import org.berendeev.roma.rssreader.data.XmlTimeParserImpl;
import org.berendeev.roma.rssreader.data.http.RssFeedHttpDataSource;
import org.berendeev.roma.rssreader.data.preferences.PreferencesDataSources;
import org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper;
import org.berendeev.roma.rssreader.data.sqlite.RssFeedSqlDataSource;
import org.berendeev.roma.rssreader.domain.HtmlImageFiller;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.domain.XmlTimeParser;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private Context context;

    public MainModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }

    @Singleton
    @Provides
    public DatabaseOpenHelper provideDatabaseOpenHelper(Context context){
        return new DatabaseOpenHelper(context);
    }

    @Singleton
    @Provides
    public XmlTimeParser provideXmlTimeParser(){
        return new XmlTimeParserImpl();
    }

    @Singleton
    @Provides
    public RssFeedHttpDataSource provideRssFeedHttpDataSource(XmlTimeParser timeParser){
        return new RssFeedHttpDataSource(timeParser);
    }

    @Singleton
    @Provides
    public RssFeedSqlDataSource provideRssFeedSqlDataSource(DatabaseOpenHelper databaseOpenHelper){
        return new RssFeedSqlDataSource(databaseOpenHelper);
    }

    @Singleton
    @Provides
    public PreferencesDataSources providePreferencesDataSources(Context context){
        return new PreferencesDataSources(context);
    }

    @Singleton
    @Provides
    public RssFeedRepository provideRssFeedRepository(PreferencesDataSources preferencesDataSources, RssFeedSqlDataSource sqlDataSource, RssFeedHttpDataSource httpDataSource){
        return new RssFeedRepositoryImpl(preferencesDataSources, sqlDataSource, httpDataSource);
    }

    @Singleton
    @Provides
    public HtmlImageFiller provideHtmlImageFiller(Context context){
        return new HtmlImageFillerImpl(context);
    }
}
