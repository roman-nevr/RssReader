package org.berendeev.roma.rssreader;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//@RunWith(RobolectricTestRunner.class)
//@Config(manifest= Config.NONE)
public class RssTest {

    @Before
    public void before(){
//        Context context = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void rss(){
        List<String> headlines = new ArrayList<>();
        List<String> links = new ArrayList<>();

        try {
            URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");
//            URL url = new URL("https://lenta.ru/rss/news");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            // We will get the XML from an input stream
            xpp.setInput(getInputStream(url), "UTF-8");

        /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
         * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
         * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
         * so we should skip the "<title>" tag which is a child of "<channel>" tag,
         * and take in consideration only "<title>" tag which is a child of "<item>"
         *
         * In order to achieve this, we will make use of a boolean variable.
         */
            boolean insideItem = false;

            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    System.out.println(xpp.getName());

                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                        System.out.println("inside item --------");
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem)
                            headlines.add(xpp.nextText()); //extract the headline
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideItem)
                            links.add(xpp.nextText()); //extract the link of article
                    }
                }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                    insideItem=false;
                }

                eventType = xpp.next(); //move to next element
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        printList(headlines);
        printList(links);
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    private void printList(List<String> list){
        System.out.println("List " + list.toString());
        for (String s : list) {
            System.out.println(s);
        }
        System.out.println("");
    }

    /*
lenta

item
inside item --------
guid
title
link
description
pubDate
enclosure
category
item
     */
/*
pcworld

item
inside item --------
title
pubDate
author
dc:creator
description
link
enclosure
categories
category
 */

/*
my

thumbnail для картинки из записи (если имеется);
● заголовок записи;
● начало содержимого.

дата и время записи (отформатированные согласно региональным настройкам устройства);
● автор записи;
● картинка из записи (если имеется);
● содержимое записи из RSS-ленты (это обычно сокращенный текст записи).

thumbnail
title
link
author
pubDate
enclosure

 */

}
