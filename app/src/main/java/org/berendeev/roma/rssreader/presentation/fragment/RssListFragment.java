package org.berendeev.roma.rssreader.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.domain.HtmlImageFiller;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.berendeev.roma.rssreader.presentation.App;
import org.berendeev.roma.rssreader.presentation.adapter.RssListAdapter;
import org.berendeev.roma.rssreader.presentation.controller.RssListController;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter.RssListRouter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class RssListFragment extends Fragment{

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_container) SwipeRefreshLayout refreshLayout;
//    @BindView(R.id.settings) ImageButton settings;

    private RssListAdapter adapter;
    private RssFeedRepository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RssListController controller;
    private HtmlImageFiller filler;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof RssListRouter)){
            throw new IllegalArgumentException("activity must implement RssListRouter");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_list, container, false);
        initDi();
        initUi(view);
        return view;
    }

    @Override public void onStart() {
        super.onStart();
        subscribeOnRssFeed();
//        updateFeed();
    }

    @Override public void onStop() {
        super.onStop();
        compositeDisposable.clear();

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onResume() {
        super.onResume();
    }

    private void subscribeOnRssFeed() {
        compositeDisposable.add(repository
                .getFeedObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rssItems -> {
                    setRssFeed(rssItems);
                }));
    }

    private void updateFeed() {
        repository.updateFeed()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    refreshLayout.setRefreshing(false);
                });
    }

    private void setRssFeed(List<RssItem> rssItems) {
        if (adapter == null){
            adapter = new RssListAdapter(rssItems, controller, getActivity().getApplicationContext(), filler);
            recyclerView.setAdapter(adapter);
        }else {
            adapter.update(rssItems);
        }
    }

    private void initUi(View view) {
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        if (adapter != null){
            recyclerView.setAdapter(adapter);
        }

        refreshLayout.setOnRefreshListener(() -> {
            updateFeed();
        });

//        settings.setOnClickListener(v -> {
//            controller.showSettings();
//        });
    }

    private void initDi() {
        repository = App.getInstance().getMainComponent().rssFeedRepository();
        filler = App.getInstance().getMainComponent().htmlImageFiller();
        //activity is instance of BaseRouter, we checked it in onAttach()
        controller = new RssListController((RssListRouter) getActivity());
    }
}
