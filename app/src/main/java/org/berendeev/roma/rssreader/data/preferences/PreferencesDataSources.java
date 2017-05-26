package org.berendeev.roma.rssreader.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesDataSources {
    public static final String RSS_LINK = "rssLink";
    public static final String LINK = "link";
    public static final String NAME = "name";
    private Context context;
    private SharedPreferences rssPreferences;

    public PreferencesDataSources(Context context) {
        this.context = context;
        rssPreferences = context.getSharedPreferences(RSS_LINK, Context.MODE_PRIVATE);
    }

    public void saveNewFeed(String link, String name){
        rssPreferences.edit()
                .putString(LINK, link)
                .putString(NAME, name)
                .apply();
    }

    public String getLink(){
//        saveNewFeed("http://feeds.pcworld.com/pcworld/latestnews");
//        return "http://feeds.pcworld.com/pcworld/latestnews";
        return rssPreferences.getString(LINK, "http://feeds.pcworld.com/pcworld/latestnews");
    }

    public String getName(){
        return rssPreferences.getString(NAME, "PCWorld");
    }
}
