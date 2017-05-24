package org.berendeev.roma.rssreader;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Util {
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html, Html.ImageGetter imageGetter){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        } else {
            result = Html.fromHtml(html, imageGetter, null);
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
