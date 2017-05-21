package org.berendeev.roma.rssreader.presentation.controller;

import org.berendeev.roma.rssreader.presentation.router.RssListRouter;

public class RssListController {

    private RssListRouter router;

    public RssListController(RssListRouter router) {
        this.router = router;
    }

    public void onItemClick(String link){
        //Todo move to preview
        router.moveToPreview(link);
    }
}
