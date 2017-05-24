package org.berendeev.roma.rssreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PicassoTest {

    private Context context;
    private boolean finish;
    private Picasso picasso;

    @Before
    public void before() {
        context = RuntimeEnvironment.application.getApplicationContext();
        initPicasso();

    }

    private void initPicasso(){

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttpDownloader(context, getCacheMaxSize()));
        picasso = builder.build();
        if (true){
            picasso.setIndicatorsEnabled(true);
            picasso.setLoggingEnabled(true);
        }

        Picasso.setSingletonInstance(picasso);
    }

    private int getCacheMaxSize(){
        return 10 * 1024 * 1024;
    }

    @Test
    public void fetch() throws InterruptedException {
        List<String> links = new ArrayList<>();
        links.add("https://cms-images.idgesg.net/images/article/2015/06/maingear-closeup-1-100589718-large.jpg");
        links.add("http://images.techhive.com/images/article/2017/05/james_gosling_joins_aws-100723510-large.jpg");
        finish = false;
        fetchAll(links, new Callback() {
            @Override public void onSuccess() {
                finish();
            }

            @Override public void onError() {

            }
        });
        while (!finish){
            Thread.sleep(1000);
        }
    }

    private void finish(){
        finish = true;
    }

    private void fetchAll(List<String> links, Callback callback) {
        Observable.fromIterable(links)
                .flatMap(s -> Observable.just(s)
                        .subscribeOn(Schedulers.io())
                        .map(this::mapLink)
                        .map(this::mapBitmap))
                .observeOn(Schedulers.single())
                .lastElement()
                .subscribe(link -> {
                    System.out.println("finish");
                    System.out.println(link.toString());
                    System.out.println(link.getIntrinsicWidth());
                    System.out.println(link.getIntrinsicHeight());
                    callback.onSuccess();
                });
    }

    private Bitmap mapLink(String link) {
        try {
            System.out.println("start loading of " + link);
            Bitmap bitmap = Picasso.with(context)
                    .load(link)
                    .get();
            System.out.println("loading of " + link + " finished");
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Drawable mapBitmap(Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }

    @Test
    public void fetch2() throws InterruptedException {
        List<String> links = new ArrayList<>();
        links.add("https://cms-images.idgesg.net/images/article/2015/06/maingear-closeup-1-100589718-large.jpg");
        links.add("http://images.techhive.com/images/article/2017/05/james_gosling_joins_aws-100723510-large.jpg");

        Callback callback = new Callback() {
            @Override public void onSuccess() {
                System.out.println("finish");
            }

            @Override public void onError() {

            }
        };

        fetchAll2(links, callback);
        while (!finish){
            Thread.sleep(100);
            Observable.just(links.get(0))
                    .subscribeOn(Schedulers.io())
                    .map(link -> nullMap(link))
                    .subscribe(r -> {
                        System.out.println("");
                    }, throwable -> {});

        }


    }

    private Bitmap nullMap(String link) throws IOException {
        return picasso
                .load(link)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .get();
    }

    private void fetchAll2(List<String> links, Callback callback) {
        Observable.fromIterable(links)
                .flatMap(s -> {
                    return getFetchCompletable(s).toObservable();
                })
                .subscribe(o -> {
                    finish();
                });
    }

    private Completable getFetchCompletable(String link){
        return Completable.create(emitter -> {
            System.out.println("start loading of " + link);
            Target target = new Target() {
                @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                }

                @Override public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            picasso
                    .load(link)
//                    .fetch();
                    .fetch(new Callback() {
                        @Override public void onSuccess() {
                            System.out.println("loading of " + link + " finished");
                            emitter.onComplete();
                        }

                        @Override public void onError() {
                            System.out.println("loading of " + link + " finished");
                            emitter.onComplete();
                        }
                    });
        });
    }

    @Test
    public void cacheTest() throws InterruptedException {
        String link = "https://cms-images.idgesg.net/images/article/2015/06/maingear-closeup-1-100589718-large.jpg";
        loadImage(context, link, new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                System.out.println(from);
            }

            @Override public void onBitmapFailed(Drawable errorDrawable) {
                System.out.println("fail");
            }

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
                System.out.println("prep");
            }
        });



        picasso.load(link).fetch();

        Thread.sleep(100000);
    }

    private void loadImage(Context context, String imageUrl, Target into){

        Target target = new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                System.out.println("onBitmapLoaded");
                into.onBitmapLoaded(bitmap, from);
            }

            @Override public void onBitmapFailed(Drawable errorDrawable) {
                System.out.println("onBitmapFailed");
                Picasso.with(context)
                        .load(imageUrl)
//                                .error(R.drawable.header)
                        .into(into);
            }

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
                System.out.println("onPrepareLoad");
            }
        };

        Picasso.with(context)
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(target);
    }
}
