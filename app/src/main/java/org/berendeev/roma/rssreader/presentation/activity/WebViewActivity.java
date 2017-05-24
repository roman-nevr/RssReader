package org.berendeev.roma.rssreader.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.presentation.fragment.WebViewFragment;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter;

public class WebViewActivity extends AppCompatActivity implements BaseRouter{

    public static final String LINK = "link";
    private String link;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);
        readLink();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null){
            showWebView();
        }
    }

    public void showWebView() {
        beginTransaction();
        addListFragment(WebViewFragment.getInstance(link));
        commitTransaction();
    }

    private void beginTransaction(){
        transaction = fragmentManager.beginTransaction();
    }

    private void commitTransaction(){
        transaction.commit();
    }

    private void addListFragment(Fragment fragment) {
        transaction.add(R.id.container, fragment);
    }

    private void readLink(){
        link = getIntent().getStringExtra(LINK);
    }

    public static void start(Context context, String link){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(LINK, link);
        context.startActivity(intent);
    }

    @Override public void moveBack() {
        finish();
    }
}
