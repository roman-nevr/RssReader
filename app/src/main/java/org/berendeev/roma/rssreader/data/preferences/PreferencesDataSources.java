package org.berendeev.roma.rssreader.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesDataSources {
    public static final String RSS_LINK = "rssLink";
    public static final String LINK = "link";
    private Context context;
    private SharedPreferences rssLinkPreferences;

    public PreferencesDataSources(Context context) {
        this.context = context;
        rssLinkPreferences = context.getSharedPreferences(RSS_LINK, Context.MODE_PRIVATE);
    }

    public void saveLink(String link){
        rssLinkPreferences.edit()
                .putString(LINK, link)
                .apply();
    }

    public String getLink(){
        return rssLinkPreferences.getString(LINK, "");
    }
}
