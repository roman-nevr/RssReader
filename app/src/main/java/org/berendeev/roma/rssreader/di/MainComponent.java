package org.berendeev.roma.rssreader.di;

import org.berendeev.roma.rssreader.domain.HtmlImageFiller;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.presentation.fragment.RssListFragment;
import org.berendeev.roma.rssreader.presentation.fragment.RssPreviewFragment;
import org.berendeev.roma.rssreader.presentation.fragment.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
//    void inject (RssListFragment fragment);

    RssFeedRepository rssFeedRepository();

    HtmlImageFiller htmlImageFiller();

    void inject(SettingsFragment fragment);

    void inject(RssPreviewFragment fragment);
}
