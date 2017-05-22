package org.berendeev.roma.rssreader.presentation.router;

public interface Navigator {

    void moveBack();

    interface RssListRouter extends Navigator{
        void moveToPreview(String link);

        void showSettings();
    }

    interface RssPreviewRouter extends Navigator{
        void showArticle(String link);
    }

}
