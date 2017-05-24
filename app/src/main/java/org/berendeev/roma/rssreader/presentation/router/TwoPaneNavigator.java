package org.berendeev.roma.rssreader.presentation.router;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.presentation.fragment.FullSizeViewFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssListFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssPreviewFragment;

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
        showFragment(fragment);
    }

    @Override public void showSettings() {
        //todo
    }

    @Override public void showRssList() {
        beginTransaction();
        addListFragment(new RssListFragment());
        commitTransaction();
    }


    @Override public void moveBack() {
        if(fragmentManager.getBackStackEntryCount() < 1){
            activity.finish();
        }else {
            fragmentManager.popBackStack();
        }
    }

    @Override public void showArticle(String link) {
        Fragment fragment = FullSizeViewFragment.getInstance(link);
        showFragment(fragment);
    }

    private void beginTransaction(){
        transaction = fragmentManager.beginTransaction();
    }

    private void commitTransaction(){
        transaction.commit();
    }

    private void showFragment(Fragment fragment){
        beginTransaction();
        setAnimation();
        replaceFragmentWith(fragment);
        commitTransaction();
    }

    private void addListFragment(Fragment fragment) {
        transaction.add(R.id.list_container, fragment);
        transaction.addToBackStack(null);
    }

    private void replaceFragmentWith(Fragment fragment) {
        transaction.replace(R.id.preview_container, fragment);
        transaction.addToBackStack(null);
    }

    private void setAnimation(){
        int enter = R.anim.to_right_in;
        int exit = R.anim.to_left_out;
        int popEnter = R.anim.to_left_in;
        int popExit = R.anim.to_right_out;

//        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
    }
}
