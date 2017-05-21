package org.berendeev.roma.rssreader.data;

import org.berendeev.roma.rssreader.domain.XmlTimeParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class XmlTimeParserImpl implements XmlTimeParser{

    @Override public long parse(String pubDate) throws ParseException {
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
}
