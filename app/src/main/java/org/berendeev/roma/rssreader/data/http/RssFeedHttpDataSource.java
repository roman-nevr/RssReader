package org.berendeev.roma.rssreader.data.http;

import android.support.v7.appcompat.BuildConfig;

import org.berendeev.roma.rssreader.data.XmlTimeParser;
import org.berendeev.roma.rssreader.data.entity.HttpRssItem;
import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RssFeedHttpDataSource {

    XmlTimeParser timeParser;

    public RssFeedHttpDataSource(XmlTimeParser timeParser) {
        this.timeParser = timeParser;
    }

    public List<RssItem> getFeed(URL url){
        try {
            XmlPullParser xpp = getPullParser(url);
            List<RssItem> rssItems = new ArrayList<>();
            while (!isDocumentEnd(xpp)){
                xpp.next();
                if ("item".equalsIgnoreCase(getStartTagName(xpp))){

                    HttpRssItem httpRssItem = new HttpRssItem();
                    do{
                        xpp.nextTag();
                        putNextValueIntoHttpRssItem(httpRssItem, xpp);
                    }while (!"item".equalsIgnoreCase(xpp.getName()));
                    rssItems.add(mapRssItem(httpRssItem));
                }
            }
            return rssItems;
        }catch (XmlPullParserException | IOException e){
            if (BuildConfig.DEBUG){
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
    }

    public boolean isRssAvailable(URL url){
        try {
            XmlPullParser xpp = getPullParser(url);
            boolean available = false;
            while (!isDocumentEnd(xpp) && !available){
                xpp.next();
                if ("rss".equalsIgnoreCase(getStartTagName(xpp))){
                    available = true;
                }
            }
            return available;
        }catch (XmlPullParserException | IOException e){
            if (BuildConfig.DEBUG){
                e.printStackTrace();
            }
            return false;
        }
    }

    private InputStream getInputStream(URL url) throws IOException{
        return url.openConnection().getInputStream();
    }

    private RssItem mapRssItem(HttpRssItem httpRssItem){
        return RssItem.create(
                httpRssItem.getTitle(),
                httpRssItem.getLink(),
                httpRssItem.getAuthor(),
                parsePubDate(httpRssItem.getPubDate()),
                httpRssItem.getThumbnail(),
                httpRssItem.getEnclosure());
    }

    private long parsePubDate(String pubDate){
        if (pubDate.isEmpty()){
            return 0;
        }
//        SimpleDateFormat parser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH);
        try {
            return timeParser.parse(pubDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void putNextValueIntoHttpRssItem(HttpRssItem httpRssItem, XmlPullParser xpp) throws XmlPullParserException, IOException {
        if (isStartTag(xpp)){
            String tag = xpp.getName();
            String text;
            if("enclosure".equalsIgnoreCase(tag)){
                text = xpp.getAttributeValue(0);
            }else {
                xpp.next();
                if (isText(xpp)) {
                    text= xpp.getText();
                }else {
                    text = "";
                }
            }
            httpRssItem.setField(tag, text.trim());
        }
    }

    private XmlPullParser getPullParser(URL url) throws IOException, XmlPullParserException{
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(getInputStream(url), "UTF-8");
        return xpp;
    }

    private boolean isDocumentEnd(XmlPullParser xpp) throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.END_DOCUMENT;
    }

    private boolean isStartTag(XmlPullParser xpp) throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.START_TAG;
    }

    private boolean isText(XmlPullParser xpp) throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.TEXT;
    }

    private String getStartTagName(XmlPullParser xpp) throws XmlPullParserException, IOException {
        while (!isStartTag(xpp) && !isDocumentEnd(xpp)){
            xpp.nextToken();
        }
        String name = xpp.getName();
        return name;
    }
}
