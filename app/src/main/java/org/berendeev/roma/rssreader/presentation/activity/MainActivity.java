package org.berendeev.roma.rssreader.presentation.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.presentation.fragment.RssListFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssPreviewFragment;
import org.berendeev.roma.rssreader.presentation.router.RssListRouter;

public class MainActivity extends AppCompatActivity implements RssListRouter {

    public static final String PREVIEW = "preview";
    public static final String LIST = "list";
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null){
            beginTransaction();
            showFirstFragment(LIST);
            commitTransaction();
        }
    }

    private void showFirstFragment(String tag){
        Fragment fragment = new RssListFragment();
        transaction.add(R.id.container, fragment, tag);
    }

    private void beginTransaction(){
        transaction = fragmentManager.beginTransaction();
    }

    private void commitTransaction(){
        transaction.commit();
    }

    private void showFragment(Fragment fragment, String tag){
        beginTransaction();
        setAnimation();
        putFragmentOnLayout(fragment,tag);
        commitTransaction();
    }

    private void putFragmentOnLayout(Fragment fragment, String tag) {
        transaction.replace(R.id.container, fragment, tag);
    }

    private void setAnimation(){
//        transaction.setCustomAnimations(R.anim.to_left_in, R.anim.to_right_out);
    }

    @Override public void moveToPreview(String link) {
        Fragment fragment = RssPreviewFragment.getInstance(link);
        showFragment(fragment, PREVIEW);
    }


}
