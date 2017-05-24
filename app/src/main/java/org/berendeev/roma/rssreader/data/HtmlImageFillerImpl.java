package org.berendeev.roma.rssreader.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.berendeev.roma.rssreader.BuildConfig;
import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.Util;
import org.berendeev.roma.rssreader.domain.HtmlImageFiller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.berendeev.roma.rssreader.Util.fromHtml;

public class HtmlImageFillerImpl implements HtmlImageFiller {

    public HtmlImageFillerImpl(Context context) {
        this.context = context;
    }

    private Context context;

    @Override public void fill(TextView textView, String html) {
        Html.ImageGetter defaultGetter = new Html.ImageGetter() {
            @Override public Drawable getDrawable(String source) {
                VectorDrawableCompat drawableCompat = VectorDrawableCompat.create(context.getResources(), R.drawable.empty, context.getTheme());
                drawableCompat.setBounds(0, 0, 1, 1);
                return drawableCompat;
            }
        };
        String withoutImg = html.replaceAll("<img.+?>", "");
        Spanned spanned = Util.fromHtml(withoutImg, defaultGetter);
        String text = spanned.toString().replaceAll("\n", "").trim();
        textView.setText(text);
    }

    @Override public void fillWithImages(TextView textView, String html) {
        Spanned spanned = fromHtml(html);
        ImageSpan[] spans = spanned.getSpans(0, spanned.length(), ImageSpan.class);
        if (spans.length != 0) {
            List<String> links = new ArrayList<>();
            for (ImageSpan span : spans) {
                links.add(span.getSource());
            }
            Map<String, Drawable> drawableMap = new HashMap<>();
            Observable.fromIterable(links)
                    .flatMap(link -> Observable.just(link)
                            .subscribeOn(Schedulers.io())
                            .map(this::getDrawableFromLink)
                            .doOnNext(dr ->drawableMap.put(link, dr))
                    )
                    .lastElement()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(drawable -> {
                        Html.ImageGetter imageGetter = new PicassoImageGetter(drawableMap);
                        Spanned spanned1 = fromHtml(html, imageGetter);
                        textView.setText(spanned1);
                    });
        }
        textView.setText(spanned);
    }

    private Drawable mapBitmap(Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

    private class PicassoImageGetter implements Html.ImageGetter {

        private Map<String, Drawable> cache;

        public PicassoImageGetter(Map<String, Drawable> cache) {
            this.cache = cache;
        }

        @Override public Drawable getDrawable(String source) {
            if (source != null){
                return cache.get(source);
            }else {
                return null;
            }
        }
    }

    private Drawable getDrawableFromLink(String link) {
        Bitmap bitmap;
        try {
            bitmap = Picasso.with(context)
                    .load(link)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .get();
            return mapBitmap(bitmap);
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            try {
                bitmap = Picasso.with(context)
                        .load(link)
                        .get();
                return mapBitmap(bitmap);
            } catch (IOException e1) {
                if (BuildConfig.DEBUG) {
                    e1.printStackTrace();
                }
                return null;
            }
        }
    }
}
