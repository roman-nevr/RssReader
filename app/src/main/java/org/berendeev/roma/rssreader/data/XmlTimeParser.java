package org.berendeev.roma.rssreader.data;

import java.text.ParseException;

public interface XmlTimeParser {
    long parse(String pubDate) throws ParseException;
}
