package org.berendeev.roma.rssreader.presentation.router;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.berendeev.roma.rssreader.presentation.fragment.RssListFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssPreviewFragment;
import org.berendeev.roma.rssreader.presentation.fragment.WebViewFragment;

public abstract class Navigator implements BaseRouter.RssListRouter, BaseRouter.RssPreviewRouter {

    protected static final String RSS_LIST = "list";
    protected static final String PREVIEW = "preview";
    protected static final String WEB_VIEW = "web_view";
    protected static final String SETTINGS = "settings";


    protected final Fragment getFragmentByTag(String tag, String arg){
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment == null || fragment.isRemoving()){
            switch (tag){
                case RSS_LIST: {
                    return new RssListFragment();
                }
                case PREVIEW:{
                    return RssPreviewFragment.getInstance(arg);
                }
                case WEB_VIEW:{
                    return WebViewFragment.getInstance(arg);
                }
                default:{
                    throw new IllegalArgumentException();
                }
            }
        }else {
            return fragment;
        }
    }

    protected abstract FragmentManager getFragmentManager();
}
