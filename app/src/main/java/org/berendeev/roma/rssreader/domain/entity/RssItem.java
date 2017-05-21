package org.berendeev.roma.rssreader.domain.entity;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RssItem {

    public static RssItem EMPTY = create("", "", "", "", 0, "", "");

    public abstract String title();

    public abstract String link();

    public abstract String description();

    public abstract String author();

    public abstract long pubDate();

    public abstract String thumbnail();

    public abstract String enclosure();

    public static RssItem create(String title, String link, String description, String author, long pubDate, String thumbnail, String enclosure) {
        return builder()
                .title(title)
                .link(link)
                .description(description)
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

        public abstract Builder description(String description);

        public abstract Builder author(String author);

        public abstract Builder pubDate(long pubDate);

        public abstract Builder thumbnail(String thumbnail);

        public abstract Builder enclosure(String enclosure);

        public abstract RssItem build();
    }
}
