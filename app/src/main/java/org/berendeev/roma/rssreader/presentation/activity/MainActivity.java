package org.berendeev.roma.rssreader.presentation.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.presentation.fragment.FullSizeViewFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssListFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssPreviewFragment;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter.RssListRouter;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter.RssPreviewRouter;
import org.berendeev.roma.rssreader.presentation.router.Navigator;
import org.berendeev.roma.rssreader.presentation.router.OnePaneNavigator;
import org.berendeev.roma.rssreader.presentation.router.TwoPaneNavigator;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BaseRouter, RssListRouter, RssPreviewRouter {

    public static final String PREVIEW = "preview";
    public static final String LIST = "list";
    public static final String STACK = "stack";
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Navigator navigator;


//    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUi();
        initRouter();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            showFirstFragment();
        }
        initActionBar();

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
        return frameLayout == null;
    }

    private void initUi() {
        setContentView(R.layout.main);
        ButterKnife.bind(this);
    }

    private void initActionBar() {
//        setSupportActionBar(toolbar);
//        toolbar.animate().translationXBy(100).setDuration(getResources().getInteger(config_shortAnimTime));
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
//        if(fragmentManager.getBackStackEntryCount() < 1){
//            finish();
//        }else {
//            fragmentManager.popBackStack();
//        }
        navigator.moveBack();
    }

    @Override public void onBackPressed() {
        moveBack();
    }

    @Override public void showArticle(String link) {
//        Fragment fragment = FullSizeViewFragment.getInstance(link);
//        showFragment(fragment);
        showArticle(link);
    }
}
