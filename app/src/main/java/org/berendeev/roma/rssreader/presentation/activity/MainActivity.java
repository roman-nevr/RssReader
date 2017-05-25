package org.berendeev.roma.rssreader.presentation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter.RssListRouter;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter.RssPreviewRouter;
import org.berendeev.roma.rssreader.presentation.router.Navigator;
import org.berendeev.roma.rssreader.presentation.router.OnePaneNavigator;
import org.berendeev.roma.rssreader.presentation.router.TwoPaneNavigator;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BaseRouter, RssListRouter, RssPreviewRouter {

    public static final String OPEN_LINK = "open_link";
    private Navigator navigator;
    private String openLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initRouter();
        if (savedInstanceState == null){
            showFirstFragment();
        }
    }


    private void initRouter() {
        if (isTwoPane()){
            navigator = new TwoPaneNavigator(this);
        }else {
            navigator = new OnePaneNavigator(this);
        }
    }

    private boolean isTwoPane() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.preview_container);
        return frameLayout != null;
    }

    private void initUi() {
        setContentView(R.layout.main);
        ButterKnife.bind(this);
    }

    private void showFirstFragment(){
        navigator.showRssList();

    }

    @Override public void moveToPreview(String link) {
        navigator.moveToPreview(link);
    }

    @Override public void showSettings() {
        //todo
        navigator.showSettings();
    }

    @Override public void showRssList() {
        navigator.showRssList();
    }


    @Override public void moveBack() {
        navigator.moveBack();
    }

    @Override public void onBackPressed() {
        moveBack();
    }

    @Override public void showWebArticle(String link) {
        navigator.showWebArticle(link);
    }
}
