package org.berendeev.roma.rssreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.xml.sax.XMLReader;

import java.io.IOException;

public class Util {
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html, PicassoImageGetter imageGetter){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html, imageGetter, new MyTagHandler());
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static void fillTextViewWithHtml(TextView textView, Context context, RssItem rssItem){
        Spanned spanned = fromHtml(rssItem.description());
        ImageSpan[] spans = spanned.getSpans(0, spanned.length(), ImageSpan.class);
        if (spans.length != 0){
            for (ImageSpan span : spans) {
                Picasso.with(context)
                        .load(span.getSource())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into();
            }
//            AsyncTask<ImageSpan[], Integer, Void> asyncTask = new AsyncTask<ImageSpan[], Integer, Void>() {
//                @Override protected Void doInBackground(ImageSpan[]... params) {
//                    for (ImageSpan span : params[0]) {
//
//                    }
//                    return null;
//                }
//            };
//            asyncTask.execute(spans);
        }else {
            textView.setText(spanned);
        }
    }

    private static class PicassoImageGetter implements Html.ImageGetter {

        private Context context;

        public PicassoImageGetter(Context context) {
            this.context = context;
        }

        @Override public Drawable getDrawable(String source) {
//            Picasso.with(context)
//                    .load(source)
            return null;
        }
    }

    private static class MyTagHandler implements Html.TagHandler{
        @Override public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

        }
    }

//    public static Drawable getDrawableFromLink(Context context, String link){
//        try {
//            Bitmap bitmap = null;
//                    Picasso.with(context)
//                    .load(link)
////                    .networkPolicy(NetworkPolicy.OFFLINE)
//                    .fetch(new Callback() {
//                        @Override public void onSuccess() {
//
//                        }
//
//                        @Override public void onError() {
//
//                        }
//                    });
//            BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
//            return drawable;
//        } catch (IOException e) {
//            if(BuildConfig.DEBUG){
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

    public static void loadImage(Context context, String imageUrl, ImageView into){
        Picasso.with(context)
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(into, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(imageUrl)
//                                .error(R.drawable.header)
                                .into(into, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                        if (BuildConfig.DEBUG){
                                            Log.v("Picasso","Could not fetch image");
                                        }
                                    }
                                });
                    }
                });
    }
}
