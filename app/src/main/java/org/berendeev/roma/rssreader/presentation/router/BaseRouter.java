package org.berendeev.roma.rssreader.presentation.router;

public interface BaseRouter {

    //returns true if moved back
    void moveBack();

    interface RssListRouter extends BaseRouter {
        void moveToPreview(String link);

        void showSettings();

        void showRssList();
    }

    interface RssPreviewRouter extends BaseRouter {
        void showWebArticle(String link);
    }

}
