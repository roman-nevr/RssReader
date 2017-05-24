package org.berendeev.roma.rssreader.domain;

import android.widget.TextView;

import org.berendeev.roma.rssreader.domain.entity.RssItem;

public interface HtmlImageFiller {
    void fillWithImages(TextView textView, String html);

    void fill(TextView textView, String html);
}
