package org.berendeev.roma.rssreader;

import android.content.Context;

import org.berendeev.roma.rssreader.data.XmlTimeParser;
import org.berendeev.roma.rssreader.data.http.RssFeedHttpDataSource;
import org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper;
import org.berendeev.roma.rssreader.data.sqlite.RssFeedSqlDataSource;
import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class RssTest {

    private Context context;
    private DatabaseOpenHelper openHelper;
    private RssFeedSqlDataSource sqlDataSource;
    private RssFeedHttpDataSource rssFeedHttpDataSource;

    @Before
    public void before(){
        context = RuntimeEnvironment.application.getApplicationContext();
        openHelper = new DatabaseOpenHelper(context);
        sqlDataSource = new RssFeedSqlDataSource(openHelper);

        XmlTimeParser timeParser = new XmlTimeParser() {
            @Override public long parse(String pubDate) throws ParseException {
                return parsePubDate(pubDate);
            }
        };
        rssFeedHttpDataSource = new RssFeedHttpDataSource(timeParser);

    }

    @Test
    public void consume_rss_save_and_read_all() throws MalformedURLException {

        URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");
//            URL url = new URL("https://lenta.ru/rss/news");
        System.out.println("rss is available: " + rssFeedHttpDataSource.isRssAvailable(url));
//        System.out.println(rssFeedHttpDataSource.getFeed(url));
        List<RssItem> feed = rssFeedHttpDataSource.getFeed(url);

        sqlDataSource.saveAllRssItems(feed);

        System.out.println(sqlDataSource.getAllRssItems());
    }

    @Test
    public void save_rss_item(){
        RssItem rssItem = RssItem.create("title", "link", "me", System.currentTimeMillis(), "", "");
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(context);
        RssFeedSqlDataSource sqlDataSource = new RssFeedSqlDataSource(openHelper);

        sqlDataSource.saveRssItem(rssItem);


        System.out.println(sqlDataSource.getAllRssItems());
    }


    private void printList(List<String> list){
        System.out.println("List " + list.toString());
        for (String s : list) {
            System.out.println(s);
        }
        System.out.println("");
    }

    private long parsePubDate(String pubDate){
        if (pubDate.isEmpty()){
            return 0;
        }
        SimpleDateFormat parser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH);
        try {
            return parser.parse(pubDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Test
    public void time_format() throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH);
        DateFormatSymbols dateFormatSymbols = parser.getDateFormatSymbols();

        NumberFormat numberFormat = parser.getNumberFormat();

        System.out.println(parser.format(new Date()));

        String date = "Sat, 20 May 2017 22:41:00 +0300";
        String date2 = "Сб, 20 мая 2017 23:09:48 +0300";
        System.out.println(parser.parse(date));
    }

}
