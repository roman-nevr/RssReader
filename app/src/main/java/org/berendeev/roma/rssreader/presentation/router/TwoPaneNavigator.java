package org.berendeev.roma.rssreader.presentation.router;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.presentation.activity.WebViewActivity;
import org.berendeev.roma.rssreader.presentation.fragment.RssPreviewFragment;
import org.berendeev.roma.rssreader.presentation.fragment.SettingsFragment;

import java.util.List;

public class TwoPaneNavigator extends Navigator {

    private AppCompatActivity activity;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    public TwoPaneNavigator(AppCompatActivity activity) {
        this.activity = activity;
        fragmentManager = activity.getSupportFragmentManager();
    }

    @Override public void moveToPreview(String link) {
        Fragment fragment = RssPreviewFragment.getInstance(link);
        showFragment(fragment, PREVIEW, R.id.preview_container);
    }

    @Override public void showSettings() {
        Fragment fragment = new SettingsFragment();
        beginTransaction();
        replaceFragmentWith(fragment, SETTINGS, R.id.preview_container);
        transaction.addToBackStack(null);
        commitTransaction();
    }

    @Override public void showRssList() {
        Fragment fragment = getFragmentByTag(RSS_LIST, null);
        showFragment(fragment, RSS_LIST, R.id.list_container);
    }



    @Override public void moveBack() {
        Fragment fragment = fragmentManager.findFragmentByTag(SETTINGS);
        if (fragment != null &&  fragment.isAdded()){
            fragmentManager.popBackStack();
        }else {
            activity.finish();
        }
    }

    @Override public void showWebArticle(String link) {
        WebViewActivity.start(activity, link);
    }

    protected void beginTransaction(){
        transaction = fragmentManager.beginTransaction();
    }

    protected void commitTransaction(){
        transaction.commit();
    }

    private void showFragment(Fragment fragment, String tag, @IdRes int containerId){
        beginTransaction();
        replaceFragmentWith(fragment, tag, containerId);
        commitTransaction();
    }

    private void replaceFragmentWith(Fragment fragment, String tag, @IdRes int containerId) {
        transaction.replace(containerId, fragment, tag);
    }

    @Override protected FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}
