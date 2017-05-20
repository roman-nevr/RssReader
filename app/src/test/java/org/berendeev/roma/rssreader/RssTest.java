package org.berendeev.roma.rssreader;

import org.berendeev.roma.rssreader.data.entity.HttpRssItem;
import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(false);
//            XmlPullParser xpp = factory.newPullParser();
//
//            // We will get the XML from an input stream
//            xpp.setInput(getInputStream(url), "UTF-8");

            XmlPullParser xpp = getPullParser(url);

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

    @Test
    public void consumeFeed(){
        try {
            URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");
//            URL url = new URL("https://lenta.ru/rss/news");
            XmlPullParser xpp = getPullParser(url);
            List<RssItem> rssItems = new ArrayList<>();

            while (!isDocumentEnd(xpp)){
                xpp.next();
                if ("item".equalsIgnoreCase(getStartTagName(xpp))){

                    HttpRssItem httpRssItem = new HttpRssItem();
                    System.out.println("item start");
                    do{
                        xpp.nextTag();
                        putNextValueIntoHttpRssItem(httpRssItem, xpp);
                    }while (!"item".equalsIgnoreCase(xpp.getName()));
                    rssItems.add(mapRssItem(httpRssItem));
                }
            }
            System.out.println(rssItems);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
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
            System.out.println(tag + " " + text.trim());
        }
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

    private String getEndTagName(XmlPullParser xpp) throws XmlPullParserException, IOException {
        while (!isEndTag(xpp) && !isDocumentEnd(xpp)){
            xpp.nextToken();
        }
        String name = xpp.getName();

        return name;
    }

    private boolean isDocumentEnd(XmlPullParser xpp) throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.END_DOCUMENT;
    }

    private boolean isStartTag(XmlPullParser xpp) throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.START_TAG;
    }

    private boolean isEndTag(XmlPullParser xpp) throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.END_TAG;
    }

    private XmlPullParser getPullParser(URL url) throws IOException, XmlPullParserException{
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        // We will get the XML from an input stream
        xpp.setInput(getInputStream(url), "UTF-8");

        return xpp;
    }

    public InputStream getInputStream(URL url) throws IOException{
        return url.openConnection().getInputStream();
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

    @Test
    public void rss_availabel(){
        try {
            URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");
//            URL url = new URL("https://lenta.ru/rss/news");
            System.out.println(isRssAvailabel(url));
        }catch (MalformedURLException e){
            e.printStackTrace();
            System.out.println(false);
        }

    }

    public boolean isRssAvailabel(URL url){
        try {
            XmlPullParser xpp = getPullParser(url);
            List<RssItem> rssItems = new ArrayList<>();
            boolean available = false;
            while (!isDocumentEnd(xpp) && !available){
                xpp.next();
                if ("rss".equalsIgnoreCase(getStartTagName(xpp))){
                    available = true;
                }
            }
            return available;
        }catch (XmlPullParserException | IOException e){
            e.printStackTrace();
            return false;
        }
    }

}
