package org.berendeev.roma.rssreader.presentation.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.presentation.fragment.RssListFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssPreviewFragment;
import org.berendeev.roma.rssreader.presentation.router.Navigator;
import org.berendeev.roma.rssreader.presentation.router.Navigator.RssListRouter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Navigator, RssListRouter, Navigator.RssPreviewRouter {

    public static final String PREVIEW = "preview";
    public static final String LIST = "list";
    public static final String STACK = "stack";
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

//    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUi();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            showFirstFragment();
        }
//        initActionBar();
    }

    private void initUi() {
        setContentView(R.layout.main);
        ButterKnife.bind(this);
    }

    private void initActionBar() {
//        setSupportActionBar(toolbar);
    }

    private void showFirstFragment(){
        beginTransaction();
        addFragment(new RssListFragment());
        commitTransaction();

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

    private void addFragment(Fragment fragment) {
        transaction.add(R.id.container, fragment);
        transaction.addToBackStack(null);
    }

    private void replaceFragmentWith(Fragment fragment) {
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
    }

    private void setAnimation(){
        int enter = R.anim.to_right_in;
        int exit = R.anim.to_left_out;
        int popEnter = R.anim.to_left_in;
        int popExit = R.anim.to_right_out;

        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
    }

    @Override public void moveToPreview(String link) {
        Fragment fragment = RssPreviewFragment.getInstance(link);
        showFragment(fragment);
    }

    @Override public void showSettings() {
        //todo
    }


    @Override public void moveBack() {
        fragmentManager.popBackStack();
    }

    @Override public void onBackPressed() {
        moveBack();
    }

    @Override public void showArticle(String link) {
        //todo
    }
}
