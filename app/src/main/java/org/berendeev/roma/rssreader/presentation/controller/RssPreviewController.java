package org.berendeev.roma.rssreader.presentation.controller;

import org.berendeev.roma.rssreader.presentation.router.BaseRouter.RssPreviewRouter;

import javax.inject.Inject;

public class RssPreviewController {

    private RssPreviewRouter router;

    @Inject
    public RssPreviewController() {
    }

    public void setRouter(RssPreviewRouter router){
        this.router = router;

    }

    public void onBack(){
        router.moveBack();
    }

    public void showFullArticle(String link){
        router.showWebArticle(link);
    }
}
