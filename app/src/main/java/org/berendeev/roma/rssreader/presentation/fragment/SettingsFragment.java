package org.berendeev.roma.rssreader.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.berendeev.roma.rssreader.BuildConfig;
import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.presentation.App;
import org.berendeev.roma.rssreader.presentation.controller.SettingsController;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter;
import org.berendeev.roma.rssreader.presentation.router.Navigator;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.VISIBLE;


public class SettingsFragment extends Fragment {

    @BindView(R.id.back_button) ImageButton backButton;
    @BindView(R.id.save_button) Button saveButton;
    @BindView(R.id.cancel) Button cancelButton;
    @BindView(R.id.feed_url) EditText feedUrl;
    @BindView(R.id.pcworld_url) TextView pcworld;
    @BindView(R.id.lenta_url) TextView lenta;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Inject RssFeedRepository repository;
    @Inject SettingsController controller;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof BaseRouter)){
            throw new IllegalArgumentException("activity must implement RssPreviewRouter");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_settings, container, false);
        initDi();
        initUi(view);
        return view;
    }

    @Override public void onStop() {
        super.onStop();
        compositeDisposable.dispose();
        controller.stop();
    }

    private void initDi() {
        App.getInstance().getMainComponent().inject(this);
        controller.setNavigator((BaseRouter) getActivity());
        controller.setView(this);
    }

    private void initUi(View view) {
        ButterKnife.bind(this, view);
        initBackButton();
        initSaveButton();
        initCancelButton();

        showCurrentUrl();
        initFastUrls();
    }

    private void initBackButton() {
        backButton.setOnClickListener(v -> {
            controller.back();
        });
    }

    private void initSaveButton() {
        saveButton.setOnClickListener(v -> {
            controller.setNewUrl(feedUrl.getText().toString());
        });
    }

    private void initCancelButton() {
        cancelButton.setOnClickListener(v -> {
            controller.back();
        });
    }

    private void showCurrentUrl() {
        repository
                .getFeedUrl()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(url -> {
                    feedUrl.setText(url.toString());
                });
    }

    private void initFastUrls() {
        if (BuildConfig.DEBUG){
            lenta.setVisibility(VISIBLE);
            pcworld.setVisibility(VISIBLE);
            lenta.setOnClickListener(v -> {
                feedUrl.setText(lenta.getText());
            });
            pcworld.setOnClickListener(v -> {
                feedUrl.setText(pcworld.getText());
            });
        }
    }

    public void showUrlError() {
        feedUrl.setError(getResources().getString(R.string.feed_error));
    }

    public void showNoRssError(){
        feedUrl.setError(getResources().getString(R.string.no_rss_error));
    }

    public void showSuccess(){
        Toast.makeText(getContext(), R.string.new_feed_set, Toast.LENGTH_LONG).show();
    }

    public void showProgress(){
        progressBar.setVisibility(VISIBLE);
        saveButton.setEnabled(false);
    }

    public void hideProgress(){
        progressBar.setVisibility(View.GONE);
        saveButton.setEnabled(true);
    }
}
