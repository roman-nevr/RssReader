package org.berendeev.roma.rssreader.domain;

import java.text.ParseException;

public interface XmlTimeParser {
    long parse(String pubDate) throws ParseException;
}
