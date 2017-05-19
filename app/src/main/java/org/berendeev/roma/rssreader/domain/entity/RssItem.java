package org.berendeev.roma.rssreader.domain.entity;

import android.graphics.Bitmap;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RssItem {

    public abstract String title();

    public abstract String link();

    public abstract String author();

    public abstract long pubDate();

    public abstract String thumbnail();

    public abstract String enclosure();

    public static RssItem create(String title, String link, String author, long pubDate, String thumbnail, String enclosure) {
        return builder()
                .title(title)
                .link(link)
                .author(author)
                .pubDate(pubDate)
                .thumbnail(thumbnail)
                .enclosure(enclosure)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_RssItem.Builder();
    }

    @AutoValue.Builder public abstract static class Builder {
        public abstract Builder title(String title);

        public abstract Builder link(String link);

        public abstract Builder author(String author);

        public abstract Builder pubDate(long pubDate);

        public abstract Builder thumbnail(String thumbnail);

        public abstract Builder enclosure(String enclosure);

        public abstract RssItem build();
    }
}
