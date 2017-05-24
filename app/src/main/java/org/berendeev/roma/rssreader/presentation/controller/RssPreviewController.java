package org.berendeev.roma.rssreader.presentation.controller;

import org.berendeev.roma.rssreader.presentation.router.BaseRouter.RssPreviewRouter;

public class RssPreviewController {

    private RssPreviewRouter router;

    public RssPreviewController(RssPreviewRouter router) {
        this.router = router;
    }

    public void onBack(){
        router.moveBack();
    }

    public void showFullArticle(String link){
        router.showArticle(link);
    }
}
