package org.berendeev.roma.rssreader.presentation.router;

public interface BaseRouter {

    void moveBack();

    interface RssListRouter extends BaseRouter {
        void moveToPreview(String link);

        void showSettings();

        void showRssList();
    }

    interface RssPreviewRouter extends BaseRouter {
        void showArticle(String link);
    }

}
