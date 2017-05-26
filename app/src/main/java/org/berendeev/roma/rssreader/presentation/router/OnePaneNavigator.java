package org.berendeev.roma.rssreader.presentation.router;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.presentation.activity.WebViewActivity;
import org.berendeev.roma.rssreader.presentation.fragment.SettingsFragment;
import org.berendeev.roma.rssreader.presentation.fragment.WebViewFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssListFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssPreviewFragment;

public class OnePaneNavigator extends Navigator {

    private AppCompatActivity activity;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private static final int CONTAINER_ID = R.id.container;

    public OnePaneNavigator(AppCompatActivity activity) {
        this.activity = activity;
        fragmentManager = activity.getSupportFragmentManager();
    }

    @Override public void moveToPreview(String link) {
        Fragment fragment = getFragmentByTag(PREVIEW, link);
        showFragment(fragment, PREVIEW, CONTAINER_ID);
    }

    @Override public void showSettings() {
        //todo
        Fragment fragment = new SettingsFragment();
        showFragment(fragment, SETTINGS, CONTAINER_ID);
    }

    @Override public void showRssList() {
        Fragment fragment = getFragmentByTag(RSS_LIST, null);
        beginTransaction();
        addFragment(fragment, RSS_LIST, CONTAINER_ID);
        commitTransaction();
    }


    @Override public void moveBack() {
        if(fragmentManager.getBackStackEntryCount() < 1){
            activity.finish();
        }else {
            fragmentManager.popBackStack();
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
        setAnimation();
        replaceFragmentWith(fragment, tag, containerId);
        commitTransaction();
    }

    protected void addFragment(Fragment fragment, String tag, @IdRes int containerId) {
        transaction.replace(containerId, fragment, tag);
    }

    private void replaceFragmentWith(Fragment fragment, String tag, @IdRes int containerId) {
        transaction.replace(containerId, fragment, tag);
        transaction.addToBackStack(null);
    }

    private void setAnimation(){
        int enter = R.anim.to_right_in;
        int exit = R.anim.to_left_out;
        int popEnter = R.anim.to_left_in;
        int popExit = R.anim.to_right_out;

        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
    }

    @Override protected FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}
